package to_api;

import methods.ToHash;
import methods.ToLinkedList;
import methods.ToSet;
import methods.ToString;

public class KeyValueDataBase {

    public static ToString String() {
        return new ToString();
    }

    public static ToLinkedList LinkedList() {
        return new ToLinkedList();
    }

    public static ToHash Hash(){
        return new ToHash();
    }

    public static ToSet Set(){
        return new ToSet();
    }

}
