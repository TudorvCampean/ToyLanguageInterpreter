    package model.adts;

    import exception.MyException;
    import exception.NotDefinedException;


    import java.util.HashMap;
    import java.util.Map;

    public class MyDictionary<K,V> implements IMyDictionary<K,V> {

        private final Map<K, V> map = new HashMap<>();
        @Override
        public boolean isDefined(K key) {
            return map.containsKey(key);
        }

        @Override
        public void update(K key, V value) {
            map.put(key, value);
        }

        @Override
        public V getValue(K key) throws MyException {
            if(!isDefined(key)){
                throw new NotDefinedException("Variable " + key + " is not defined.");
            }
            return map.get(key);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<K, V> entry : map.entrySet()) {
                sb.append(entry.getKey().toString())
                        .append(" -> ")
                        .append(entry.getValue().toString())
                        .append("\n");
            }
            return sb.toString();
        }
        @Override
        public Map<K, V> getContent() {
            return map;
        }

        @Override
        public void remove(K key) {
            map.remove(key);
        }

        @Override
        public IMyDictionary<K, V> copy() {
            MyDictionary<K, V> newDict = new MyDictionary<>();
            for (Map.Entry<K, V> entry : this.map.entrySet()) {
                newDict.update(entry.getKey(), entry.getValue());
            }
            return newDict;
        }
    }
