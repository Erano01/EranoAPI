"""Mojang's sounds.json of each revision's representative version: sound event -> the files it plays."""
import json,os,urllib.request,sys
OUT=sys.argv[1]
VERS=['1.8','1.8.3','1.8.8','1.9.2','1.9.4','1.10.2','1.11.2','1.12.2','1.13','1.13.2','1.14.4','1.15.2','1.16.1','1.16.3','1.16.5','1.17.1','1.18.1','1.18.2','1.19.2','1.19.3','1.19.4','1.20.1','1.20.2','1.20.4','1.20.6','1.21.1','1.21.3','1.21.4','1.21.5','1.21.6','1.21.8','1.21.10','1.21.11','26.1.2','26.2','26.3']
def get(url): return json.load(urllib.request.urlopen(url,timeout=60))
man={v['id']:v['url'] for v in get('https://piston-meta.mojang.com/mc/game/version_manifest_v2.json')['versions']}
idx_cache={}
for v in VERS:
    p=os.path.join(OUT,v+'.json'); q=os.path.join(OUT,v+'.hashes.json')
    if os.path.exists(p) and os.path.exists(q): continue
    ai=get(man[v])['assetIndex']['url']
    if ai not in idx_cache: idx_cache[ai]=get(ai)
    # sound file -> content hash: the same audio under a moved path (1.9 moved many files)
    json.dump({k[len('minecraft/sounds/'):-4]:o['hash'] for k,o in idx_cache[ai]['objects'].items() if k.startswith('minecraft/sounds/') and k.endswith('.ogg')},open(q,'w'))
    h=idx_cache[ai]['objects']['minecraft/sounds.json']['hash']
    s=urllib.request.urlopen('https://resources.download.minecraft.net/%s/%s'%(h[:2],h),timeout=60).read()
    open(p,'wb').write(s); print(v,len(json.loads(s)))
