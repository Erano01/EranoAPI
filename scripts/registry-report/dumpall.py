import os,json,subprocess,sys
from fields import fields
M2=os.path.expanduser('~/.m2/repository/org/spigotmc/spigot-api')
def vkey(v): return [int(x) for x in v.split('.')]
CLASSES=['org.bukkit.Sound','org.bukkit.enchantments.Enchantment','org.bukkit.potion.PotionEffectType','org.bukkit.potion.PotionType','org.bukkit.Particle','org.bukkit.Effect']
out={}
for d in sorted([d for d in os.listdir(M2) if d.endswith('SNAPSHOT')],key=lambda d:vkey(d.split('-')[0])):
    v=d.split('-')[0]
    jar=[f for f in os.listdir(os.path.join(M2,d)) if f.endswith('.jar') and 'sources' not in f and 'shaded' not in f][0]
    out[v]={c:fields(os.path.join(M2,d,jar),c) for c in CLASSES}
# 1.8 has no Bukkit Particle: its particles are NMS EnumParticle's, the very names 1.9's Particle took
for v in ('1.8','1.8.3','1.8.8'):
    if v in out:
        rev={'1.8':'v1_8_R1','1.8.3':'v1_8_R2','1.8.8':'v1_8_R3'}[v]
        server=os.path.expanduser('~/.m2/repository/org/spigotmc/spigot/%s-R0.1-SNAPSHOT/spigot-%s-R0.1-SNAPSHOT.jar'%(v,v))
        f=fields(server,'net.minecraft.server.%s.EnumParticle'%rev)
        out[v]['org.bukkit.Particle']={'kind':'nms','fields':f['fields']}
json.dump(out,open(sys.argv[1],'w'))
for v in out:
    print(v,' '.join('%s=%s/%d'%(c.split('.')[-1],(out[v][c] or {}).get('kind','-')[0],len((out[v][c] or {}).get('fields',[]))) for c in CLASSES))
