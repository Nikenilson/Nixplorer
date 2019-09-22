public class NoArvore<X extends Comparable<X>>{
    protected X val;
    protected NoArvore esq, dir;

    public NoArvore(){
        this.setDir(null);
        this.setEsq(null);
        this.setVal(null);
    }

    public NoArvore(X val1){
        this.setVal(val1);
        this.setDir(null);
        this.setEsq(null);
    }

    //GETTERS

    public X getVal(){
        return this.val;
    }

    public NoArvore getEsq(){
        return this.esq;
    }

    public NoArvore getDir(){
        return this.dir;
    }

    //SETTERS
    public void setVal(X newVal){
        this.val = newVal;
    }

    public void setEsq(NoArvore esq2){
        this.esq = esq2;
    }

    public void setDir(NoArvore dir2){
        this.dir = dir2;
    }

    public int CompareTo(X outro){
        return val.CompareTo(outro);
    }
}