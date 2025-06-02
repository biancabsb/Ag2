package main;

public class IndividuoFactoryDixonPrice implements IndividuoFactory {
    private int n;

	public IndividuoFactoryDixonPrice(int n) {
		 this.n = n;	
		 }

	@Override
	public Individuo getIndividuo() {
		return new IndividuoDixonPrice(this.n);
	}

}
