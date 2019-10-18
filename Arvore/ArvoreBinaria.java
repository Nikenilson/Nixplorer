public class ArvoreBinaria<X extends Comparable<X>>{
    protected NoArvore<X> raiz;

    public ArvoreBinaria(){
        raiz = new NoArvore<X>();
    }

    public NoArvore<X> getRaiz(){
        return this.raiz;
    }

    public void setRaiz(NoArvore<X> raiz){
        this.raiz = raiz;
    }
}