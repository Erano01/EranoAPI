import os,re,json
def vkey(v): return [int(x) for x in v.split('.')]
versions=sorted([f[:-4] for f in os.listdir('out') if f.endswith('.tsv')],key=vkey)
data={}
for v in versions:
    rows={}
    for line in open('out/%s.tsv'%v):
        if line.startswith('#') or not line.strip(): continue
        p=line.rstrip('\n').split('\t')
        rows[p[0]]=dict(id=p[1],legacy=p[2]=='1',block=p[3]=='1',item=p[4]=='1',dep=p[5]=='1')
    data[v]=rows
modern=lambda v,n: not data[v][n]['legacy'] and not n.startswith('LEGACY_')
# Timeline of modern names (1.13+ without LEGACY_) and of pre-1.13 names.
pre=[v for v in versions if vkey(v)<[1,13]]
post=[v for v in versions if vkey(v)>=[1,13]]
def timeline(vs,filt):
    t={}
    for i,v in enumerate(vs):
        for n in data[v]:
            if not filt(v,n): continue
            e=t.setdefault(n,dict(first=v,prev=vs[i-1] if i>0 else None,last=v,gaps=[]))
            if e['last']!=v and vs[vs.index(e['last'])+1]!=v: e['gaps'].append(v)
            e['last']=v
    return t
tpre=timeline(pre,lambda v,n: True)
tpost=timeline(post,lambda v,n: modern(v,n))
for n,e in tpre.items(): e['id']=data[e['first']][n]['id']
changes=[]
for vs,filt in ((pre,lambda v,n:True),(post,lambda v,n:modern(v,n))):
    for a,b in zip(vs,vs[1:]):
        A={n for n in data[a] if filt(a,n)}; B={n for n in data[b] if filt(b,n)}
        changes.append(dict(frm=a,to=b,added=sorted(B-A),removed=sorted(A-B)))
json.dump(dict(versions=versions,tpre=tpre,tpost=tpost,changes=changes),open('analysis.json','w'),indent=0)
for c in changes: print(c['frm'],'->',c['to'],'+%d -%d'%(len(c['added']),len(c['removed'])),' removed:',c['removed'][:12])
print('pre names',len(tpre),'post modern names',len(tpost))
