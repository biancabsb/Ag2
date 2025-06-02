package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IndLangermann extends Individuo {
    // === Parâmetros fixos da Langermann Function ===
    private static final int M = 5;           // número de termos (constante)
    private static final double[][] A = {      // matriz A (5×2)
        {3.0, 5.0},
        {5.0, 2.0},
        {2.0, 1.0},
        {1.0, 4.0},
        {7.0, 9.0}
    };
    private static final double[] C = {1.0, 2.0, 5.0, 2.0, 3.0}; // vetor c
    
    // === Parâmetros do AG para Langermann ===
    private static final double MIN = 0.0;      // limite inferior de cada gene
    private static final double MAX = 10.0;     // limite superior de cada gene
    private static final double ALFA = 0.5;     // parâmetro do BLX-α
    private static final double DESVIO = 0.1;   // desvio padrão da mutação gaussiana

    private static final Random rand = new Random();

    private final int dim;        // dimensão (número de genes)
    private double[] genes;       // vetor de genes (tamanho = dim)

    // ----------------------------
    // → Construtor “aleatório” (usa dim para criar genes em [0,10])
    // ----------------------------
    public IndLangermann(int dim) {
        this.dim = dim;
        this.genes = new double[dim];
        for (int i = 0; i < dim; i++) {
            // cada gene inicia aleatório em [0, 10)
            genes[i] = MIN + rand.nextDouble() * (MAX - MIN);
        }
        // força avaliação imediata (e marca como “já avaliado”)
        this.avaliado = true;
        this.avaliacao = this.avaliar(); // armazenar explicitamente
		this.maximizacao = false;

    }

    // ----------------------------
    // → Construtor “a partir de um vetor de genes” (por crossover ou mutação)
    // ----------------------------
    public IndLangermann(double[] genes) {
        this.dim = genes.length;
        // CUIDADO: copiamos o vetor passado, para não manter referências externas
        this.genes = genes.clone();
        // Se algum gene veio fora de [0,10], já “clampamos” aqui:
        for (int i = 0; i < dim; i++) {
            this.genes[i] = clip(this.genes[i]);
        }
        // força avaliação imediata
        this.avaliar();
        this.avaliado = true;
    }

    public List<Individuo> recombinar(Individuo p2) {

    	IndLangermann parceiro = (IndLangermann) p2;
		double alpha =ALFA;


		double[] filho1Genes = new double[dim];
		double[] filho2Genes = new double[dim];

		for (int i = 0; i < dim; i++) {
			double x = this.genes[i];
			double y = parceiro.genes[i];
			double d = Math.abs(x - y);

			double min = Math.min(x, y) - alpha * d;
			double max = Math.max(x, y) + alpha * d;
			
			
			filho1Genes[i] = min + (max - min) * rand.nextDouble();

			filho2Genes[i] = min + (max - min) * rand.nextDouble();

		}

		IndLangermann filho1 = new IndLangermann(filho1Genes);
		IndLangermann filho2 = new IndLangermann(filho2Genes);


		return List.of(filho1, filho2);
	}

    // ----------------------------
    // → Mutação Gaussiana (perturba cada gene com N(0, DESVIO))
    // ----------------------------
    @Override
    public Individuo mutar() {
        double[] novosGenes = new double[dim];
        for (int i = 0; i < dim; i++) {
            // adiciona ruído gaussiano e clamp em [0,10]
            novosGenes[i] = clip(this.genes[i] + rand.nextGaussian() * DESVIO);
        }
        IndLangermann mutante = new IndLangermann(novosGenes);
        return mutante;
    }

    @Override
    public double avaliar() {
        // Se já estiver avaliado, retorna diretamente
        // (isso evita reavaliações desnecessárias)
        if (this.avaliado) {
            return this.avaliacao;
        }

        double soma = 0.0;
        // Para cada termo i = 0..M−1
        for (int i = 0; i < M; i++) {
            double inner = 0.0;
            // “inner” = ∑_{j=0..dim−1} (genes[j] − A[i][j])²
            // ATENÇÃO: A[i] tem apenas dimensão 2, então usamos (j % 2)
            for (int j = 0; j < dim; j++) {
                double diff = genes[j] - A[i][j % 2];
                inner += diff * diff;
            }
            // cada termo: c[i] * exp(−inner/π) * cos(π·inner)
            double termo = C[i] * Math.exp(-inner / Math.PI) * Math.cos(Math.PI * inner);
            soma += termo;
        }

        // A função de Langermann padrão é **minimizada**, 
        // portanto guardamos a avaliação como (-soma).
        this.avaliacao = soma;
        this.avaliado = true;  // marca como avaliado
        return this.avaliacao;
    }

    // ----------------------------
    // → Exibe vetor de genes formatado
    // ----------------------------
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < dim; i++) {
            // exibe 4 casas decimais, separadas por vírgula
            sb.append(String.format("%.4f", genes[i]));
            if (i < dim - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean isMaximizacao() {
        return false; 
    }

    // ----------------------------
    // → Limita (clamp) val ao intervalo [MIN, MAX]
    // ----------------------------
    private double clip(double val) {
        if (val < MIN) return MIN;
        if (val > MAX) return MAX;
        return val;
    }
}
