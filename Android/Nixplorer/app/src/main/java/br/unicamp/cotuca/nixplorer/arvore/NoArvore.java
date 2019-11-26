package br.unicamp.cotuca.nixplorer.arvore;

public class NoArvore
{
	NoArvore esquerda, direita;
	Coordenada coord;

	public NoArvore getEsquerda() {
		return esquerda;
	}

	public void setEsquerda(NoArvore esquerda) {
		this.esquerda = esquerda;
	}

	public NoArvore getDireita() {
		return direita;
	}

	public void setDireita(NoArvore direita) {
		this.direita = direita;
	}

	public Coordenada getCoord() {
		return coord;
	}

	public void setCoord(Coordenada coord) {
		this.coord = coord;
	}

	public NoArvore(Coordenada coord)
	{
		this.coord = coord;
		esquerda = direita = null;
	}
}