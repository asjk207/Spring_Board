package first.common.common;
 
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
 /*
   내부적으로 Map을 하나 생성하고, 그 맵에 모든 데이터를 담는 역할을 한다. 
   여기서 중요한점은 절대로 Map을 상속받으면 안된다.
  */
public class CommandMap {
    Map<String,Object> map = new HashMap<String,Object>();
     
    public Object get(String key){
        return map.get(key);
    }
     
    public void put(String key, Object value){
        map.put(key, value);
    }
     
    public Object remove(String key){
        return map.remove(key);
    }
     
    public boolean containsKey(String key){
        return map.containsKey(key);
    }
     
    public boolean containsValue(Object value){
        return map.containsValue(value);
    }
     
    public void clear(){
        map.clear();
    }
     
    public Set<Entry<String, Object>> entrySet(){
        return map.entrySet();
    }
     
    public Set<String> keySet(){
        return map.keySet();
    }
     
    public boolean isEmpty(){
        return map.isEmpty();
    }
     
    public void putAll(Map<? extends String, ?extends Object> m){
        map.putAll(m);
    }
     
    public Map<String,Object> getMap(){
        return map;
    }
}