"""Each 26.3 sound back through the revisions. Same event key: the same sound. Otherwise (renamed keys: 1.9's new
names, 1.13's) the event of the older revision that plays the same files (Mojang's sounds.json)."""
import json,os
from registry import vkey
D='mojang'  # run in the work folder (run.sh)
def files_of(v):
    s=json.load(open(os.path.join(D,v+'.json')))
    def res(ev,seen=()):
        out=set()
        for x in s.get(ev,{}).get('sounds',[]):
            if isinstance(x,str): out.add(x)
            elif x.get('type')=='event':
                if x['name'] not in seen: out|=res(x['name'].replace('minecraft:',''),seen+(ev,))
            else: out.add(x['name'])
        return {f.replace('minecraft:','') for f in out}
    return {ev:res(ev) for ev in s}
def hashes(v): return json.load(open(os.path.join(D,v+'.hashes.json')))
def translate(files,newer,older):
    """Newer paths as the older revision names them: a file moved to another path (same content) is the same."""
    inv={}
    for p,h in older.items(): inv.setdefault(h,p)
    return {f if f in older or newer.get(f) not in inv else inv[newer[f]] for f in files}
def build(keys):
    vs=sorted(keys,key=vkey)
    files={v:files_of(v) for v in vs}
    hs={v:hashes(v) for v in vs}
    newest=vs[-1]
    result={}; fuzzy=[]
    for name,key in keys[newest].items():
        hist={newest:(name,key)}; k=key
        for i in range(len(vs)-2,-1,-1):
            v=vs[i]; nv=vs[i+1]; by_key={kk:n for n,kk in keys[v].items()}
            if k in by_key:
                hist[v]=(by_key[k],k); continue
            want=translate(files[nv].get(k,set()),hs[nv],hs[v])
            best=None
            if want:
                for kk,n in by_key.items():
                    have=files[v].get(kk,set())
                    if not have: continue
                    # a key still in the newer revision is its own sound there, not a rename
                    if kk in files[nv] and vkey(v)>=[1,9]: continue
                    j=len(want&have)/len(want|have)
                    if j>0 and (best is None or j>best[0] or (j==best[0] and kk<best[1])): best=(j,kk,n)
            if best is None or best[0]<0.5: break
            if best[0]<1: fuzzy.append((name,v,k,best[1],round(best[0],2)))
            k=best[1]; hist[v]=(best[2],k)
        result[name]=hist
    return vs,result,fuzzy
if __name__=='__main__':
    keys=json.load(open('soundkeys.json'))
    vs,r,f=build(keys)
    json.dump(r,open('soundmap.json','w'))
    on18=sum(1 for h in r.values() if '1.8' in h); on19=sum(1 for h in r.values() if '1.9.2' in h); on113=sum(1 for h in r.values() if '1.13' in h)
    print('sounds',len(r),'on 1.8',on18,'on 1.9',on19,'on 1.13',on113,'fuzzy',len(f))
    for x in f[:40]: print(x)
    for n in ['ENTITY_PLAYER_LEVELUP','BLOCK_NOTE_BLOCK_PLING','ENTITY_LIGHTNING_BOLT_THUNDER','BLOCK_ANVIL_PLACE','ENTITY_SNOWBALL_THROW','UI_BUTTON_CLICK','ENTITY_EXPERIENCE_ORB_PICKUP','ENTITY_ENDERMAN_TELEPORT','ENTITY_GENERIC_EXPLODE','ENTITY_ITEM_PICKUP','BLOCK_CHEST_OPEN']:
        h=r[n]; print(n,{v:h[v][0] for v in ['1.8','1.9.2','1.12.2','1.13'] if v in h})
