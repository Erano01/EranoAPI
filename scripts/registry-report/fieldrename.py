"""CraftBukkit's FieldRename tables (renames since 1.13, per type), read from its source."""
import re,os
SRC=os.path.expanduser('~/MinecraftWorkspace/Buildtools/CraftBukkit/src/main/java/org/bukkit/craftbukkit/legacy/FieldRename.java')
def table(name):
    s=open(SRC).read()
    i=s.index('FieldRenameData %s ='%name); j=s.index('.build();',i)
    return re.findall(r'\.change\("(\w+)", "(\w+)"\)',s[i:j])
