package main;

public class IndPowellFactory implements IndividuoFactory {
    private int n;

	public IndPowellFactory(int n) {
		 this.n = n;	
	}

	@Override
	public Individuo getIndividuo() {
		return new IndPowell(this.n);

	}

}
