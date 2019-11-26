package br.unicamp.cotuca.nixplorer.arvore;

public class Coordenada
{
	protected String nome;
	protected int ra;
	protected int dec;
	protected int id;

	public Coordenada(String nome, int ra, int dec, int id) throws Exception
	{
		setNome	 (nome);
		this.ra   = ra;
		this.dec  = dec;
		this.id   = id;
	}

	public Coordenada()
	{
		this.nome = null;
		this.ra   = 0;
		this.dec  = 0;
		this.id   = 0;
	}

	public String getNome()
	{
		return this.nome;
	}

	public int getRa()
	{
		return this.ra;
	}

	public int getDec()
	{
		return this.dec;
	}

	public int getId()
	{
		return this.id;
	}

	public void setNome(String nome) throws Exception
	{
		if(nome == null || nome.equals(""))
			throw new Exception("Nome vazio!");

		this.nome = nome;
	}

	public void setRa(int ra)
	{
		this.ra = ra;
	}

	public void setDec(int dec)
	{
		this.dec = ra;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int hashCode()
	{
		int i = 666;

		i = i * 7 + nome		    .hashCode();
		i = i * 7 + new Integer(ra ).hashCode();
		i = i * 7 + new Integer(dec).hashCode();
		i = i * 7 + new Integer(id ).hashCode();

		return i;
	}

	public int compareTo(Coordenada outro)
	{
		return this.id - outro.getId();
	}

	public String toString()
	{
		return "Id: " + this.id + ", Nome: " + this.nome + ", Rigth Ascension: " + this.ra + ", Declination: " + this.dec + ";";
	}

}