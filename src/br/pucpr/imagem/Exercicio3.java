package br.pucpr.imagem;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Neste exercício implementamos as funções de erosão, dilatação, abertura e fechamento. Essas funções usam um kernel
 * de 3x3 booleans. Valores com true serão considerados, com false desconsiderados. Foi também importada a função de
 * subtração da aula 1, uma vez que o exercício solicita a produção de bordas. Todas as funções possuem um parametro
 * para definir o número de repetições da operação.
 */
public class Exercicio3 {
    /** Caminho para a a pasta de imagens da aula */
    public static final File PATH = new File("/Users/vinigodoy/img");

    /**
     * Garante que o valor do pixel estará entre 0 e 255.
     */
    private int clamp(float value) {
        int v = (int)value;
        return v > 255 ? 255 : (v < 0 ? 0 : v);
    }

    /**
     * Converte os valores de r, g e b para o inteiro da cor.
     * Os valores podem estar fora do intervalo de 0 até 255, pois
     * a função ajusta chamando a função clamp (acima).
     */
    private int toColor(float r, float g, float b) {
        return new Color(clamp(r), clamp(g), clamp(b)).getRGB();
    }

    /**
     * Salva a imagem no disco.
     */
    private void salvar(BufferedImage img, String name) throws IOException {
        ImageIO.write(img, "jpg", new File(name + ".jpg"));
        System.out.printf("Salvo %s.png%n", name);
    }

    /**
     * Subtrai duas imagens do mesmo tamanho (ver exercício 3a da aula 1). Considera a imagem em tons de cinza.
     */
    public BufferedImage subtract(BufferedImage img1, BufferedImage img2) {
        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
                //Le o pixel
                Color p1 = new Color(img1.getRGB(x, y));
                Color p2 = new Color(img2.getRGB(x, y));

                //Faz a subtração, canal a canal
                int tone = p1.getRed() - p2.getRed();

                //Define a cor de saída na imagem de saída (out).
                out.setRGB(x, y, toColor(tone, tone, tone));
            }
        }

        return out;
    }

    /**
     * Operação de erosão morfológica.
     * Nesta operação buscamos entre o pixel e seus vizinhos aqueles com o tom de cinza mais escuro (de menor valor).
     * Os pixels considerados na busca são aqueles marcados com true no kernel.
     */
    public BufferedImage erode(BufferedImage img, boolean[][] kernel) {
        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //A erosão busca pelo pixel de menor valor
                int min = 255;
                //Para cada pixel percorrido na imagem, precisamos percorrer os seus 9 vizinhos
                //Os vizinhos que serão considerados estão marcados como true no kernel
                for (int ky = 0; ky < 3; ky++) {
                    for (int kx = 0; kx < 3; kx++) {
                        //Observe que os índices de kx e ky variam de 0 até 2. Já os vizinhos de x seriam
                        //x+(-1), x+0 + x+1. Por isso, subtraímos 1 de kx e ky para chegar no vizinho.
                        int px = x + (kx-1);
                        int py = y + (ky-1);

                        //Nas bordas, px ou py podem acabar caindo fora da imagem. Quando isso ocorre, pulamos para o
                        // próximo pixel.
                        if (px < 0 || px >= img.getWidth() || py < 0 || py >= img.getHeight()) {
                            continue;
                        }

                        //Obtem o tom de cinza do pixel
                        int tone = new Color(img.getRGB(px, py)).getRed();

                        //Se ele for mais escuro que o menor já encontrado, substitui
                        if (kernel[kx][ky] && tone < min) {
                            min = tone;
                        }
                    }
                }

                //Define essa cor na imagem de saída.
                out.setRGB(x, y, new Color(min, min, min).getRGB());
            }
        }
        return out;
    }

    /**
     * Aplica a erosao times vezes.
     */
    public BufferedImage erode(BufferedImage img, int times, boolean[][] kernel) {
        BufferedImage out = img;
        for (int i = 0; i < times; i++) {
            out = erode(out, kernel);
        }
        return out;
    }

    public BufferedImage dilate(BufferedImage img, boolean[][] kernel) {
        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //A dilatação busca pelo pixel de maior valor
                int max = 0;
                //Para cada pixel percorrido na imagem, precisamos percorrer os seus 9 vizinhos
                //Os vizinhos que serão considerados estão marcados como true no kernel
                for (int ky = 0; ky < 3; ky++) {
                    for (int kx = 0; kx < 3; kx++) {
                        //Observe que os índices de kx e ky variam de 0 até 2. Já os vizinhos de x seriam
                        //x+(-1), x+0 + x+1. Por isso, subtraímos 1 de kx e ky para chegar no vizinho.
                        int px = x + (kx-1);
                        int py = y + (ky-1);

                        //Nas bordas, px ou py podem acabar caindo fora da imagem. Quando isso ocorre, pulamos para o
                        // próximo pixel.
                        if (px < 0 || px >= img.getWidth() || py < 0 || py >= img.getHeight()) {
                            continue;
                        }

                        //Obtem o tom de cinza do pixel
                        int tone = new Color(img.getRGB(px, py)).getRed();

                        //Se ele for mais claro que o maior já encontrado, substitui
                        if (kernel[kx][ky] && tone > max) {
                            max = tone;
                        }
                    }
                }

                //Define essa cor na imagem de saída.
                out.setRGB(x, y, new Color(max, max, max).getRGB());
            }
        }
        return out;
    }

    /**
     * Aplica a dilatação times vezes.
     */
    public BufferedImage dilate(BufferedImage img, int times, boolean[][] kernel) {
        BufferedImage out = img;
        for (int i = 0; i < times; i++) {
            out = dilate(out, kernel);
        }
        return out;
    }

    /**
     * Abertura morfológica.
     * Trata-se de várias erosões seguidas do mesmo número de dilatações. Isso faz com que áreas pequenas da imagem
     * tendam a desaparecer, e estruturas maiores sejam mantidas.
     */
    public BufferedImage open(BufferedImage img, int times, boolean[][] kernel) {
        return dilate(erode(img, times, kernel), times, kernel);
    }

    /**
     * Fechamento morfológico.
     * Trata-se de várias dilatações seguidas do mesmo número de erosões. Isso faz com que "buracos" pequenos na imagem
     * tendam a desaparecer.
     */
    public BufferedImage close(BufferedImage img, int times, boolean[][] kernel) {
        return erode(dilate(img, times, kernel), times, kernel);
    }

    public void run() throws IOException {
        BufferedImage lizard = ImageIO.read(new File(PATH, "gray/lizard.jpg"));
        //Criação de um kernel em cruz
        boolean[][] CROSS = {
                {false, true, false},
                { true, true,  true},
                {false, true, false}
        };

        //Potencia da operação morfológica. Quanto maior, mais grossa será a borda
        int TIMES = 2;


        //Há duas formas de gerar bordas. Iremos usar as duas abaixo. As imagens intermediárias também serão salvas

        //Forma 1: Dilatar as áreas claras e subtrair do resultado a imagem original
        //bordas = dilatada - original
        BufferedImage lizardDilated = dilate(lizard, TIMES, CROSS);
        salvar(lizardDilated, "lizardDilated");
        salvar(subtract(lizardDilated, lizard), "lizardBorderDilate");

        //Forma 2: Reduzir as áreas claras com a erosão e subtraí-las da imagem original
        //bordas = original - erodida
        BufferedImage lizardEroded = erode(lizard, TIMES, CROSS);
        salvar(lizardEroded, "lizardEroded");
        salvar(subtract(lizard, lizardEroded), "lizardBorderErode");
    }

    public static void main(String[] args) throws IOException {
        new Exercicio3().run();
    }
}
