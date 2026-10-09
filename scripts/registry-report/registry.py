"""Shared by the registry reports: NMS revisions, a value's history across revisions with its renames."""
REV={'1.8':('V1_8_R1','1.8 - 1.8.2'),'1.8.3':('V1_8_R2','1.8.3'),'1.8.8':('V1_8_R3','1.8.4 - 1.8.9'),
'1.9.2':('V1_9_R1','1.9 - 1.9.3'),'1.9.4':('V1_9_R2','1.9.4'),'1.10.2':('V1_10_R1','1.10.x'),'1.11.2':('V1_11_R1','1.11.x'),
'1.12.2':('V1_12_R1','1.12.x'),'1.13':('V1_13_R1','1.13'),'1.13.2':('V1_13_R2','1.13.1 - 1.13.2'),'1.14.4':('V1_14_R1','1.14.x'),
'1.15.2':('V1_15_R1','1.15.x'),'1.16.1':('V1_16_R1','1.16 - 1.16.1'),'1.16.3':('V1_16_R2','1.16.2 - 1.16.3'),
'1.16.5':('V1_16_R3','1.16.4 - 1.16.5'),'1.17.1':('V1_17_R1','1.17.x'),'1.18.1':('V1_18_R1','1.18 - 1.18.1'),
'1.18.2':('V1_18_R2','1.18.2'),'1.19.2':('V1_19_R1','1.19 - 1.19.2'),'1.19.3':('V1_19_R2','1.19.3'),'1.19.4':('V1_19_R3','1.19.4'),
'1.20.1':('V1_20_R1','1.20 - 1.20.1'),'1.20.2':('V1_20_R2','1.20.2'),'1.20.4':('V1_20_R3','1.20.3 - 1.20.4'),
'1.20.6':('V1_20_R4','1.20.5 - 1.20.6'),'1.21.1':('V1_21_R1','1.21 - 1.21.1'),'1.21.3':('V1_21_R2','1.21.2 - 1.21.3'),
'1.21.4':('V1_21_R3','1.21.4'),'1.21.5':('V1_21_R4','1.21.5'),'1.21.6':('V1_21_R5','1.21.6 - 1.21.8'),'1.21.8':('V1_21_R5','1.21.6 - 1.21.8'),
'1.21.10':('V1_21_R6','1.21.9 - 1.21.10'),'1.21.11':('V1_21_R7','1.21.11'),'26.1.2':('V26_1','26.1.x'),'26.2':('V26_2','26.2.x'),
'26.3':('V26_3','26.3+')}
def vkey(v): return [int(x) for x in v.split('.')]
def rev(v): return '%s (%s)'%REV[v]

def history(api,cls,renames,key_of=None):
    """renames: [(old, new)] (CraftBukkit's tables). Returns (versions, entries by newest name, unmatched removals).
    An entry: first / last version, the name it had in each version, key, numeric id."""
    versions=sorted(api,key=vkey)
    names_in={v:[f[0] for f in (api[v][cls] or {}).get('fields',[])] for v in versions}
    data_in={v:{f[0]:f[1] for f in (api[v][cls] or {}).get('fields',[])} for v in versions}
    new_of=dict(renames)
    def newest(n):
        seen=set()
        while n in new_of and n not in seen: seen.add(n); n=new_of[n]
        return n
    entries={}
    for v in versions:
        for n in names_in[v]:
            e=entries.setdefault(newest(n),dict(first=v,last=v,names={},key=None,id=None))
            e['last']=v; e['names'].setdefault(v,n); e.setdefault('all',{}).setdefault(v,[]).append(n)
            for x in data_in[v][n]:
                if isinstance(x,str) and x!=n and (':' in x or '.' in x or x.islower()): e['key']=x
            ints=[x for x in data_in[v][n] if isinstance(x,int)]
            if cls.endswith('Enchantment') and ints and vkey(v)<[1,13]: e['id']=ints[0]
            if cls.endswith('PotionEffectType') and ints: e['id']=ints[0]
    # Renames as the dumps show them: the old name's last version right before the new one's first.
    checks=[]
    for o,n in renames:
        lo=max((v for v in versions if o in names_in[v]),key=vkey,default=None)
        fn=min((v for v in versions if n in names_in[v]),key=vkey,default=None)
        checks.append((o,n,lo,fn,lo is not None and fn is not None and versions.index(fn)==versions.index(lo)+1))
    return versions,entries,checks,names_in
