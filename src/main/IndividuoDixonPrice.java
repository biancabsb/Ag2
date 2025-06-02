package main;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class IndividuoDixonPrice extends Individuo {
	private double genes[];
	private int n;

	public IndividuoDixonPrice(int n) {
		this.n = n;
		this.genes = new double[n];
		Random rand = new Random();
		for (int i = 0; i < n; i++) {
			this.genes[i] = (rand.nextDouble() * 20) - 10;
		}
		this.maximizacao = false;
	}
	public IndividuoDixonPrice(double[] genes) {
		 this.n = genes.length;
	        this.genes = genes;
	        this.maximizacao = false;
	        avaliar();

    }

	@Override
	public List<Individuo> recombinar(Individuo p2) {

		IndividuoDixonPrice parceiro = (IndividuoDixonPrice) p2;
		double alpha = 1.0;
		Random rand = new Random();


		double[] filho1Genes = new double[n];
		double[] filho2Genes = new double[n];

		for (int i = 0; i < n; i++) {
			double x = this.genes[i];
			double y = parceiro.genes[i];
			double d = Math.abs(x - y);

			double min = Math.min(x, y) - alpha * d;
			double max = Math.max(x, y) + alpha * d;
			
			
			filho1Genes[i] = min + (max - min) * rand.nextDouble();

			filho2Genes[i] = min + (max - min) * rand.nextDouble();

		}

		IndividuoDixonPrice filho1 = new IndividuoDixonPrice(n);
		IndividuoDixonPrice filho2 = new IndividuoDixonPrice(n);
		filho1.genes = filho1Genes;
		filho2.genes = filho2Genes;

		return List.of(filho1, filho2);
	}

	@Override
	public Individuo mutar() {
		Random rand = new Random();
		double sigma = 0.5;
		double probMutacao = 0.3;

		double[] novosGenes = new double[n];
		for (int i = 0; i < n; i++) {
			if (rand.nextDouble() < probMutacao) {
				double delta = rand.nextGaussian() * sigma;
				novosGenes[i] = genes[i] + delta;
				if (novosGenes[i] > 10)
					novosGenes[i] = 10;
				if (novosGenes[i] < -10)
					novosGenes[i] = -10;
			} else {
				novosGenes[i] = genes[i];
			}
		}

		IndividuoDixonPrice mutante = new IndividuoDixonPrice(novosGenes);
		return mutante;
	}

	@Override
	public double avaliar() {
		double x1 = this.genes[0];
		double term1 = (x1 - 1) * (x1 - 1);

		double soma = 0.0;

		for (int i = 1; i < this.genes.length; i++) {
			double xi = this.genes[i];
			double xold = this.genes[i - 1];
			double temp = 2 * xi * xi - xold;
			double aux = (i + 1) * temp * temp;
			soma += aux;
		}

		double y = term1 + soma;
		this.avaliacao = y;
		return avaliacao; //
	}

	public double[] getGenes() {
		return genes;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < genes.length; i++) {
			sb.append(String.format("%.4f", genes[i]));
			if (i < genes.length - 1)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

}
