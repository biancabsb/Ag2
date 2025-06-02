package main;

public class IndFactoryLangermann implements IndividuoFactory {
    private int n;

	public IndFactoryLangermann(int n) {
		 this.n = n;	
	}

	@Override
	public Individuo getIndividuo() {
		return new IndLangermann(this.n);
	}

}
