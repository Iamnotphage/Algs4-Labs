import edu.princeton.cs.algs4.Picture;
import java.awt.Color;


public class SeamCarver {
    // Maximum allowed memory is ~ 12 n^2 bytes.
    private int[][] color; // 4*W*H Bytes : 4 Bytes: alpha, red, green, blue;
    // why use int[][] to store the RGB value? why not Picture?
    // : avoid call Picture::getRGB() too many times which costs

    private int H; // 4 Bytes
    private int W; // 4 Bytes

    private double[][] energy; // 8*W*H Bytes

    // validate: the indices x and y are integers between 0 and width − 1 and
    // between 0 and height − 1 respectively, where width is the width of the
    // current image and height is the height. Throw an IllegalArgumentException
    // if either x or y is outside its prescribed range.
    private void validate(int x, int y){
        if(x < 0 || x > this.width() - 1 || y < 0 || y > this.height() - 1){
            throw new IllegalArgumentException();
        }
    }

    private void validate(Picture picture){
        if(picture == null){
            throw new IllegalArgumentException();
        }
    }

    // Throw an IllegalArgumentException if removeVerticalSeam() or removeHorizontalSeam() is called with an array
    // of the wrong length or if the array is not a valid seam (i.e., either an entry is outside its prescribed
    // range or two adjacent entries differ by more than 1).
    // Throw an IllegalArgumentException if removeVerticalSeam() is called when the width of the picture is less
    // than or equal to 1 or if removeHorizontalSeam() is called when the height of the picture is less than or equal to 1.
    private void validate(int[] seam, boolean isVertical){
        if(seam == null){
            throw new IllegalArgumentException();
        }

        if(isVertical){
            // if the pic's width or height is less than or equal to 1 so than can't remove a seam
            if(W <= 1){
                throw new IllegalArgumentException();
            }

            // length of int[] should be H
            // and the entry should be inside the prescribed range
            if(seam.length != H){
                throw new IllegalArgumentException();
            }else{
                // check the entry
                for (int j = 0; j < seam.length; j++) {

                    if (seam[j] < 0 || seam[j] > W - 1) {
                        throw new IllegalArgumentException();
                    }

                    // check the differs
                    if(j != seam.length - 1){
                        int diff = seam[j + 1] - seam[j];
                        if(diff == -1 || diff == 0 || diff == 1){

                        }else{
                            throw new IllegalArgumentException();
                        }
                    }

                }

            }

        }else{
            // if the pic's width or height is less than or equal to 1 so than can't remove a seam
            if(H <= 1){
                throw new IllegalArgumentException();
            }

            // length of int[] should be W
            if(seam.length != W){
                throw new IllegalArgumentException();
            }else{
                // check the entry
                for (int j = 0; j < seam.length; j++) {

                    if (seam[j] < 0 || seam[j] > H - 1) {
                        throw new IllegalArgumentException();
                    }

                    // check the differs
                    if(j != seam.length - 1){
                        int diff = seam[j + 1] - seam[j];
                        if(diff == -1 || diff == 0 || diff == 1){

                        }else{
                            throw new IllegalArgumentException();
                        }
                    }

                }
            }
        }



    }

    // init the energy & calculate the Dual-Gradient
    private void initEnergy(){

        // init the energy value:
        // on the boundary the energy set to be 1000.0
        // others set to be Dual-Gradient

        this.energy = new double[H][W];

        for(int y = 0; y < H; y++){
            for(int x = 0; x < W; x++){

                if(y == 0 || y == H - 1 ||
                        x == 0 || x == W - 1){
                    this.energy[y][x] = 1000.0;
                    continue;
                }

                this.energy[y][x] = calculateEnergy(this.color, x, y);
            }
        }
    }

    // Returns the color of pixel (col, row) as an int. Using this method can be more efficient
    // than get(int, int) because it does not create a Color object.
    // (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blue).
    //  int rgb = picture.getRGB(y, x + 1);
    //  int a = (rgb >> 24) & 0xFF;
    //  int r = (rgb >> 16) & 0xFF;
    //  int g = (rgb >>  8) & 0xFF;
    //  int b = (rgb) & 0xFF;
    private double calculateEnergy(int[][] color, int x, int y){
        // Rx = R(x + 1, y) - R(x - 1, y)
        double Rx = getRed(color[y][x + 1]) - getRed(color[y][x - 1]);
        double Gx = getGreen(color[y][x + 1]) - getGreen(color[y][x - 1]);
        double Bx = getBlue(color[y][x + 1]) - getBlue(color[y][x - 1]);
        double DeltaXSquare = Rx * Rx + Gx * Gx + Bx * Bx;

        // Ry = R(x, y + 1) - R(x, y - 1)
        double Ry = getRed(color[y + 1][x]) - getRed(color[y - 1][x]);
        double Gy = getGreen(color[y + 1][x]) - getGreen(color[y - 1][x]);
        double By = getBlue(color[y + 1][x]) - getBlue(color[y - 1][x]);
        double DeltaYSquare = Ry * Ry + Gy * Gy + By * By;

        return Math.sqrt(DeltaXSquare + DeltaYSquare);
    }

    // helper methods to get int
    private int getRed(int rgb){
        return (rgb >> 16) & 0xFF;
    }
    private int getGreen(int rgb){
        return (rgb >> 8) & 0xFF;
    }
    private int getBlue(int rgb){
        return (rgb) & 0xFF;
    }

    private void initColor(Picture picture){
        this.color = new int[H][W];
        for(int y = 0; y < H; y++){
            for(int x = 0; x < W; x++){
                this.color[y][x] = picture.getRGB(x, y);
            }
        }
    }

    /**The width(), height(), and energy() methods should take constant time in the worst case. All other methods
     * should run in time at most proportional to width × height in the worst case. For faster performance, do not
     * construct explicit DirectedEdge and EdgeWeightedDigraph objects.
    * */

    // create a seam carver object based on the given picture
    // Caution: the array's indices are different from Picture's pixels indices
    // pixel(x,y) == a[y][x]
    // ---------------------------->x
    // |
    // |          pixel(x,y)
    // |
    // |
    // V
    // y

    /** About Cache:
     * The order in which you traverse the pixels (row-major order vs. column-major order)
     * can make a big difference
     * -----------------------------------------------------------------------------------
     * Why?: Cache will preload a pixel's nearby memory
     * and the 2D array is putted in row-major order in memory
     * if we visit the column ([1][0]) it will pre-load [1][0] [1][1] [1][2] [1][3]
     * and if we then visit [2][0] cache will miss!!!!!!
     * but if we traverse the pixels by row-major order, cache will hit!!!
     * */
    public SeamCarver(Picture picture){
        validate(picture);

        // The data type may not mutate the Picture argument to the constructor.
        this.H = picture.height();
        this.W = picture.width();

        // init color
        initColor(picture);
        // energy : dual-gradient
        // init the energy
        initEnergy();
    }

    // current picture
    public Picture picture(){
        Picture picture = new Picture(W, H);
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                Color color = new Color(this.color[y][x]);
                picture.set(x, y, color);
            }
        }
        return picture;
    }


    // width of current picture
    public int width(){
        return W;
    }


    // height of current picture
    public int height(){
        return H;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y){
        validate(x, y);
        return this.energy[y][x];
    }


    // sequence of indices for horizontal seam
    // The behavior of findHorizontalSeam() is analogous to that of findVerticalSeam() except that it returns an array
    // of length width such that entry x is the row number of the pixel to be removed from column x of the image.
    public int[] findHorizontalSeam(){
        int[] res;

        trans(this.color);
        res = findVerticalSeam();
        trans(this.color);

        return res;
    }


    // sequence of indices for vertical seam
    // The findVerticalSeam() method returns an array of length H such that entry y is the column number of the pixel
    // to be removed from row y of the image.
    public int[] findVerticalSeam(){
        // distTo[y][x]: min distance to (x,y)
        // pathTo[y][x]: x-coordinate of the min path to (x,y)
        int[] seam = new int[H];
        int[][] pathTo = new int[H][W];
        double[][] distTo = new double[H][W];

        // init distTo
        for(int y = 0; y < H; y++){
            for(int x = 0; x < W; x++){
                if(y == 0){
                    distTo[y][x] = 0.0; // from source to source
                }else{
                    distTo[y][x] = Double.POSITIVE_INFINITY;
                }
            }
        }

        // relax adjacent pixels : topological order
        for(int y = 0; y < H; y++){
            for(int x = 0; x < W; x++){
                relax(x - 1, y + 1, x, y, distTo, pathTo);
                relax(x, y + 1, x, y, distTo, pathTo);
                relax(x + 1, y + 1, x, y, distTo, pathTo);
            }
        }

        // find min distance in the last row
        double min = Double.POSITIVE_INFINITY;
        int minx = 0;
        for(int x = 0; x < W; x++){
            if(min > distTo[H - 1][x]){
                min = distTo[H - 1][x];
                minx = x;
            }
        }

        // find seam in reverse order
        for(int y = 0; y < H; y++){
            seam[H - y - 1] = minx;
            minx = pathTo[H - y - 1][minx];
        }
        return seam;
    }

    // (x, y) is the next pixel, s is the previous pixel's x-coordinate
    private void relax(int x, int y, int pre_x, int pre_y, double[][] distTo, int[][] pathTo){
        if(x >= 0 && x <= W - 1 && y >=0 && y <= H - 1){
            double w = energy[y][x];

            if(distTo[y][x] > distTo[pre_y][pre_x] + w){
                distTo[y][x] = distTo[pre_y][pre_x] + w;
                pathTo[y][x] = pre_x;
            }
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam){
        validate(seam, false);
        // pic and energy should be changed

        // if we call amount times removeHorizontalSeam(), this flip boolean method
        // would help save time
        // since we don't need to
        // trans(pic); res = findVerticalSeam(); trans(pic)
        // which costs

        trans(this.color);

        removeVerticalSeam(seam);

        trans(this.color);
    }

    // remove vertical seam from current picture
    // energy should be re-calculated but just change two cols(in this case)
    public void removeVerticalSeam(int[] seam){
        validate(seam, true);

        int[][] newColor = new int[H][W - 1];
        // double[][] newEnergy = new double[H][W - 1];
        // new Picture: (W - 1) * H
        for(int y = 0; y < H; y++){
            for(int x = 0; x < W - 1; x++){
                if(x == seam[y]){
                    // seam carving
                    while(x < W - 1){
                        newColor[y][x] = this.color[y][x + 1];
                        //newEnergy[y][x] = this.energy[y][x + 1];
                        x++;
                    }
                }else{
                    // just copy
                    newColor[y][x] = this.color[y][x];
                    //newEnergy[y][x] = this.energy[y][x];
                }
            }
        }
        // re-calculate energy: only for two cols(in this vertical case)
        // only for (x - 1, y) and (x, y) : but I failed so I re-calculate the whole energy

        this.color = newColor;
        this.W = W - 1;
        initEnergy();
    }

    private void trans(int[][] color){
        int newH = W;
        int newW = H;
        int[][] newColor = new int[newH][newW];
        double[][] newEnergy = new double[newH][newW];

        for(int y = 0; y < newH; y++){
            for(int x = 0; x < newW; x++){
                newColor[y][x] = color[x][y];
                newEnergy[y][x] = this.energy[x][y];
            }
        }

        this.color = newColor;
        this.H = newH;
        this.W = newW;
        this.energy = newEnergy;
    }


    //  unit testing (optional)
    public static void main(String[] args){

    }

}