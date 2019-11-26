public class Arvore
{
	NoArvore raiz;

	public void inserirCoordenada(Coordenada d) throws Exception
	{
		if(d == null)
			throw new Exception("Coordenada vazio");
		else
		{
			/*if()
				throw new Exception("Coordenada já existente");*/
			//else
				inserirNoArvore(raiz, d);
		}
	}

	public boolean removerCoordenada(Coordenada d) throws Exception
	{
		if(d == null)
			throw new Exception("Coordenada vazio");

		if(removerNoArvore(raiz, d) == null)
			return false;

		return true;
	}

	public NoArvore procurarNoArvore(NoArvore atual, Coordenada coord)
	{
		if (coord.equals(atual.coord))
			return atual;

		/*if (coord.compareTo(atual.coord) < 0 )
			atual.esquerda = procurarNoArvore(atual.esquerda, coord);

		else if (coord.compareTo(atual.coord) > 0 )
			atual.direita = procurarNoArvore(atual.direita, coord);*/

		return null;
	}

	private NoArvore inserirNoArvore(NoArvore atual, Coordenada coord)
	{
		if (atual == null) //Raiz nula
		{
	        return new NoArvore(coord);
		}
		else if (coord.compareTo(atual.coord) < 0 )
		{
	        atual.esquerda = inserirNoArvore(atual.esquerda, coord);
		}
		else if (coord.compareTo(atual.coord) > 0 )
		{
		    atual.direita = inserirNoArvore(atual.direita, coord);
		}

    return atual;

	}

	private NoArvore removerNoArvore(NoArvore atual, Coordenada coord)
	{
		if (atual == null)
			return null;

		if (coord.equals(atual.coord))
		{
			if (atual.esquerda == null && atual.direita == null)
			    atual = null;

			else if (atual.esquerda == null)
				atual = atual.direita;
			else if (atual.direita == null)
				atual = atual.esquerda;

			if(atual.esquerda != null && atual.direita != null)
			{
				Coordenada aux = preocuraMenorCoordenada(atual.direita);
				atual.coord = aux;
				atual.direita = removerNoArvore(atual.direita, aux);
				atual = atual.direita;
			}
		}

		else if (coord.compareTo(atual.coord) < 0 )
		 	atual.esquerda = removerNoArvore(atual.esquerda, coord);

		else if (coord.compareTo(atual.coord) > 0 )
		 	atual.direita = removerNoArvore(atual.direita, coord);

    return atual;

	}

	private Coordenada preocuraMenorCoordenada(NoArvore atual) {
	    return atual.esquerda == null ? atual.coord : preocuraMenorCoordenada(atual.esquerda);
	    //Se a esquerda for nula ele eh o menor no
	}

}