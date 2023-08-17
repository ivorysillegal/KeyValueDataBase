package to_api;

import command.HashCommand;
import command.LinkedListCommand;
import command.SetCommand;
import command.StringCommand;

public class KeyValueDataBase {

    public static StringCommand String() {
        return new StringCommand();
    }

    public static LinkedListCommand LinkedList() {
        return new LinkedListCommand();
    }

    public static HashCommand Hash(){
        return new HashCommand();
    }

    public static SetCommand Set(){
        return new SetCommand();
    }

}
