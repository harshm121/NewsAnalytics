package Backend;
import java.io.Serializable;

public class pair<T1,T2> implements Serializable{

    private T1 var1;
    private T2 var2;

    public pair(T1 var1,T2 var2){
        this.var1 = var1;
        this.var2 = var2;
    }

    public T1 getVar1() {
        return var1;
    }

    public T2 getVar2() {
        return var2;
    }

    public void setVar1(T1 var1) {
        this.var1 = var1;
    }

    public void setVar2(T2 var2) {
        this.var2 = var2;
    }
}
