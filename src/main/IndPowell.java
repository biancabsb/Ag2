package main;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class IndPowell extends Individuo {
    private static final Random rand = new Random();

    private double genes[];
    private int n;

    public IndPowell(int n) {
        if (n % 4 != 0) {
            throw new IllegalArgumentException("Dimensão deve ser múltipla de 4 para função Powell.");
        }
        this.n = n;
        this.genes = new double[n];
        for (int i = 0; i < n; i++) {
            this.genes[i] = -4 + (5 + 4) * rand.nextDouble(); // intervalo [-4,5]
        }
        this.maximizacao = false;
        
    }


    public IndPowell(double[] genes) {
        this.n = genes.length;
        this.genes = genes;
        this.maximizacao = false;
        avaliar();
    }

    @Override
    public List<Individuo> recombinar(Individuo p2) {
    	IndPowell parceiro = (IndPowell) p2;
        double alpha = 0.5;

        double[] filho1Genes = new double[n];
        double[] filho2Genes = new double[n];

        for (int i = 0; i < n; i++) {
            double x = this.genes[i];
            double y = parceiro.genes[i];
            double d = Math.abs(x - y);
            double min = Math.min(x, y) - alpha * d;
            double max = Math.max(x, y) + alpha * d;

            // Garantir que os genes fiquem dentro de [-4, 5]
            filho1Genes[i] = Math.max(-4, Math.min(5, min + (max - min) * rand.nextDouble()));
            filho2Genes[i] = Math.max(-4, Math.min(5, min + (max - min) * rand.nextDouble()));
        }

        IndPowell filho1 = new IndPowell(filho1Genes);
        IndPowell filho2 = new IndPowell(filho2Genes);
        return List.of(filho1, filho2);
    }

    @Override
    public Individuo mutar() {
        double sigma = 0.3;
        double probMutacao = 0.3;

        double[] novosGenes = new double[n];
        for (int i = 0; i < n; i++) {
            if (rand.nextDouble() < probMutacao) {
                double delta = rand.nextGaussian() * sigma;
                novosGenes[i] = genes[i] + delta;
                if (novosGenes[i] > 5) novosGenes[i] = 5;
                if (novosGenes[i] < -4) novosGenes[i] = -4;
            } else {
                novosGenes[i] = genes[i];
            }
        }

        return new IndPowell(novosGenes);
    }

    @Override
    public double avaliar() {
        double soma = 0.0;
        int m = n / 4; // Powell avalia em blocos de 4 variáveis
        for (int i = 0; i < m; i++) {
            int idx = 4 * i;
            double x1 = genes[idx];
            double x2 = genes[idx + 1];
            double x3 = genes[idx + 2];
            double x4 = genes[idx + 3];

            double t1 = (x1 + 10 * x2) * (x1 + 10 * x2);
            double t2 = 5 * (x3 - x4) * (x3 - x4);
            double t3 = Math.pow(x2 - 2 * x3, 4);
            double t4 = 10 * Math.pow(x1 - x4, 4);

            soma += t1 + t2 + t3 + t4;
        }
        this.avaliacao = soma;
        return avaliacao;
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
            if (i < genes.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
