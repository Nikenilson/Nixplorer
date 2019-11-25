public class NoArvore
{
	NoArvore esquerda, direita;
	Coordenada coord;

	public NoArvore(Coordenada coord)
	{
		this.coord = coord;
		esquerda = direita = null;
	}


}