"""Bukkit Sound field -> Minecraft sound event, per revision: CraftBukkit's CraftSound before 1.16 (git history),
the API's own keys from 1.16 on (read from the bytecode)."""
import re,subprocess,json,os
CB=os.path.expanduser('~/MinecraftWorkspace/Buildtools/CraftBukkit')
F='src/main/java/org/bukkit/craftbukkit/CraftSound.java'
# The CraftBukkit commit of each revision's representative version ("Update to Minecraft ..."; the last commit
# before the next update where a revision had several)
COMMIT={'1.8':'24557bc2b','1.8.3':'d8a9c7be4','1.8.8':'de5c26123','1.9.2':'6e527e5b8','1.9.4':'a8a4bedd2^',
'1.10.2':'75f99ec7c','1.11.2':'a86731306','1.12.2':'9a1f5ee80','1.13':'1a6b4f539','1.13.2':'b4230a9a7',
'1.14.4':'e73aabd66','1.15.2':'7ea3c040b^','1.16.1':'9c9fb593f^','1.16.3':'3af81c717^'}
def craftsound(v):
    s=subprocess.run(['git','-C',CB,'show',COMMIT[v]+':'+F],capture_output=True,text=True,check=True).stdout
    m=dict(re.findall(r'set\((\w+), "([^"]+)"\)',s)) or dict(re.findall(r'^\s+(\w+)\("([^"]+)"\)',s,re.M))
    return m
def all_keys(api):
    out={}
    for v in api:
        f=api[v]['org.bukkit.Sound']['fields']
        if v in COMMIT: out[v]=craftsound(v)
        else: out[v]={n:[x for x in d if isinstance(x,str) and x!=n][0] for n,d in f}
        names={n for n,_ in f}
        assert set(out[v])==names,(v,len(set(out[v])^names),list(set(out[v])^names)[:5])
    return out
if __name__=='__main__':
    api=json.load(open('api.json'))
    k=all_keys(api); json.dump(k,open('soundkeys.json','w'))
    for v in k: print(v,len(k[v]),list(k[v].items())[3])
