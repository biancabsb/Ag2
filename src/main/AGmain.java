package main;

import java.util.Scanner;

public class AGmain {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Escolha: 1-Powell, 2-Langermann, 3-DixonPrice: ");
        int opcao = sc.nextInt();

        IndividuoFactory indFac;

        int nGenes = 20, nPop = 100, nElite = 4, nGer = 2000;

        if (opcao == 1) {
            indFac = new IndPowellFactory(nGenes);
        } else if (opcao == 2) {
        	nGenes = 2;
            indFac = new IndFactoryLangermann(nGenes);
        } else if (opcao == 3) {
            indFac = new IndividuoFactoryDixonPrice(nGenes);
        } else {
            System.out.println("Opção inválida.");
            sc.close();
            return;
        }

        AG ag = new AG();
        Individuo melhor = ag.executar(indFac, nGer, nElite, nPop);

        System.out.println("Melhor solução: " + melhor);
        System.out.printf("Avaliação: %.6f\n", melhor.getAvaliacao());

        sc.close();
    }
}
