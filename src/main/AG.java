package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class AG {
    public Individuo executar(IndividuoFactory factory, int nGer, int nElite, int nPop) {
        List<Individuo> popIni = new ArrayList<>(nPop);
        Individuo melhorGlobal = null;

        for (int i = 0; i < nPop; i++) {
            Individuo ind = factory.getIndividuo();
            ind.avaliar(); // <<< aqui!
            popIni.add(ind);		
        }

        for (int g = 0; g < nGer; g++) {
            List<Individuo> filhos = getFilhos(popIni);
            List<Individuo> mutantes = getMutantes(popIni);

            List<Individuo> popAux = new ArrayList<>(nPop * 3);
            
            popAux.addAll(filhos);
            popAux.addAll(mutantes);
            popAux.addAll(popIni);

            popIni = selecao(popAux, nElite, nPop);

            Individuo melhor = popIni.get(0);

            System.out.printf("Geração %d: Genes: %s, Avaliação: %.4f\n", g, melhor.toString(), melhor.getAvaliacao());
            if (melhorGlobal == null || melhor.getAvaliacao() < melhorGlobal.getAvaliacao()) {
                melhorGlobal = melhor;
            }
        }

        return melhorGlobal;
    }

    private List<Individuo> selecao(List<Individuo> popAux, int nElite, int nPop) {
        List<Individuo> newPop = new ArrayList<>();
        Individuo ind0 = popAux.get(0);

        if (ind0.isMaximizacao()) {
            popAux.sort(Comparator.comparingDouble(Individuo::getAvaliacao).reversed()); // Maximização
        } else {
            popAux.sort(Comparator.comparingDouble(Individuo::getAvaliacao)); // Minimização
        }

        newPop = new ArrayList<>(popAux.subList(0, nElite));

        double somaFitness = 0;
        for (Individuo ind : popAux) {
            double fitness;
            if (ind0.isMaximizacao()) {
                fitness = ind.getAvaliacao(); 
            } else {
                fitness = 1.0 / (1 + ind.getAvaliacao()); // Minimização
            }
            somaFitness += fitness;
        }

        Random rand = new Random();
        while (newPop.size() < nPop) {
            double ponto = rand.nextDouble() * somaFitness;
            double acumulado = 0;

            for (Individuo ind : popAux) {
                double fitness;
                if (ind0.isMaximizacao()) {
                    fitness = ind.getAvaliacao();
                } else {
                    fitness = 1.0 / (1 + ind.getAvaliacao());
                }

                acumulado += fitness;
                if (acumulado >= ponto) {
                    newPop.add(ind);
                    break;
                }
            }
        }

        return newPop;
    }

    private List<Individuo> getMutantes(List<Individuo> popIni) {
        List<Individuo> mutantesList = new ArrayList<>();

        for (Individuo ind : popIni) {
            Individuo mutante = ind.mutar();
            mutante.avaliar(); // ← AQUI
            mutantesList.add(mutante);
        }
        return mutantesList;
    }

    private List<Individuo> getFilhos(List<Individuo> popIni) {
        List<Individuo> filhosList = new ArrayList<>();
        List<Individuo> aux = new ArrayList<>(popIni);
        Collections.shuffle(aux);

        for (int i = 0; i < aux.size() - 1; i += 2) {
            List<Individuo> filhos = aux.get(i).recombinar(aux.get(i + 1));
            for (Individuo f : filhos) {
                f.avaliar(); // ← AQUI
            }
            filhosList.addAll(filhos);
        }

        if (aux.size() % 2 != 0) {
            Individuo ultimo = aux.get(aux.size() - 1);
            // Escolhe um parceiro aleatório (pode ser ele mesmo, se necessário)
            Random rand = new Random();
            int parceiroIndex = rand.nextInt(aux.size() - 1);
            Individuo parceiro = aux.get(parceiroIndex);
            List<Individuo> filhos = ultimo.recombinar(parceiro);
            for (Individuo f : filhos) {
                f.avaliar(); // ← AQUI
            }
            filhosList.addAll(filhos);
        }

        return filhosList;
    }
}
