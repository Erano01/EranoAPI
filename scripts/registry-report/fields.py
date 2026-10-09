"""Static fields of a class's own type and what its static initializer loads before each (strings, ints), read from
the bytecode with javap: no class is loaded, so registry-backed types (1.20.5+) work without a server."""
import re,subprocess,sys,json
JAVAP='/usr/lib/jvm/java-25-openjdk/bin/javap'
def fields(jar,cls):
    out=subprocess.run([JAVAP,'-c','-p','-constants','-cp',jar,cls],capture_output=True,text=True).stdout
    if not out: return None
    kind='enum' if re.search(r'^public final class \S+ extends java.lang.Enum',out,re.M) else 'interface' if re.search(r'^public interface',out,re.M) else 'class'
    own=cls
    names=re.findall(r'^\s*public static final %s (\w+);'%re.escape(own),out,re.M)
    i=out.find('static {};')
    body=out[i:] if i>=0 else ''
    result={}; pending=[]
    for line in body.split('\n'):
        m=re.search(r'ldc(?:_w)?\s+#\d+\s+// String (.*)$',line)
        if m: pending.append(m.group(1)); continue
        m=re.search(r'(?:bipush|sipush)\s+(-?\d+)',line) or re.search(r'iconst_(\d)',line)
        if m: pending.append(int(m.group(1))); continue
        m=re.search(r'putstatic\s+#\d+\s+// Field (\w+):L([\w/$]+);',line)
        if m:
            if m.group(2).replace('/','.')==own and m.group(1) in names and m.group(1) not in result:
                result[m.group(1)]=pending
            pending=[]
    return dict(kind=kind,fields=[(n,result.get(n,[])) for n in names])
if __name__=='__main__':
    print(json.dumps(fields(sys.argv[1],sys.argv[2]),indent=0)[:1500])
