import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    // Maximum allowed memory is ~ 12 n^2 bytes.
    private Picture picture; // 4*W*H Bytes

    private int H; // 4 Bytes
    private int W; // 4 Bytes

    private double[][] energy; // 8*W*H Bytes

    private double[][] energyTrans; // 8*W*H Bytes

    private int[] horizontalSeam; // an array of length W; 4*W Bytes

    private int[] verticalSeam; // an array of length H; 4*H Bytes


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
        for(int y = 0; y < H; y++){
            for(int x = 0; x < W; x++){

                if(y == 0 || y == H - 1 ||
                        x == 0 || x == W - 1){
                    this.energy[y][x] = 1000.0;
                    continue;
                }

                // Returns the color of pixel (col, row) as an int. Using this method can be more efficient
                // than get(int, int) because it does not create a Color object.
                // (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blue).
                //  int rgb = picture.getRGB(y, x + 1);
                //  int a = (rgb >> 24) & 0xFF;
                //  int r = (rgb >> 16) & 0xFF;
                //  int g = (rgb >>  8) & 0xFF;
                //  int b = (rgb) & 0xFF;


                // Rx = R(x + 1, y) - R(x - 1, y)
                double Rx = getRed(picture.getRGB(x + 1, y)) - getRed(picture.getRGB(x - 1, y));
                double Gx = getGreen(picture.getRGB(x + 1, y)) - getGreen(picture.getRGB(x - 1, y));
                double Bx = getBlue(picture.getRGB(x + 1, y)) - getBlue(picture.getRGB(x - 1, y));
                double DeltaXSquare = Rx * Rx + Gx * Gx + Bx * Bx;

                // Ry = R(x, y + 1) - R(x, y - 1)
                double Ry = getRed(picture.getRGB(x, y + 1)) - getRed(picture.getRGB(x, y - 1));
                double Gy = getGreen(picture.getRGB(x, y + 1)) - getGreen(picture.getRGB(x, y - 1));
                double By = getBlue(picture.getRGB(x, y + 1)) - getBlue(picture.getRGB(x, y - 1));
                double DeltaYSquare = Ry * Ry + Gy * Gy + By * By;

                this.energy[y][x] = Math.sqrt(DeltaXSquare + DeltaYSquare);
            }
        }
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
        this.picture = new Picture(picture);
        this.H = this.picture.height();
        this.W = this.picture.width();

        // return value for HorizontalSeam() and VerticalSeam()
        this.horizontalSeam = new int[W];
        this.verticalSeam = new int[H];

        // energy : dual-gradient
        this.energy = new double[H][W];

        // init the energy
        initEnergy();

        // energy's Trans: y->x x->y
        this.energyTrans = TransEnergy(this.energy);

    }

    private double[][] TransEnergy(double[][] energy){
        double[][] energyTrans = new double[W][H];
        for(int y = 0; y < H; y++){
            for(int x = 0; x < W; x++){
                energyTrans[x][y] = energy[y][x];
            }
        }
        return energyTrans;
    }


    private int[] SP(int W, int H, boolean isVertical){
        // For faster performance, do not construct explicit DirectedEdge and EdgeWeightedDigraph objects.

        if(W == 1){
            if(isVertical){
                return new int[H];
            }else{
                return new int[1];
            }
        }else if(H == 1){
            if(isVertical){
                return new int[1];
            }else{
                return new int[W];
            }
        }

        // init the EdgeWeightedDigraph; V ∈ [0, H * W - 1]
        EdgeWeightedDigraph ewd = new EdgeWeightedDigraph(W * H);
        int[] seam = new int[isVertical ? H : W];

        if(isVertical){
            // store x
            for(int v = 0; v < ewd.V(); v++){
                for(int w : neighborOfV(v, H, W)){
                    if(w != -1){
                        int[] index = Number2Index(w, W);
                        ewd.addEdge(new DirectedEdge(v, w, energy[index[1]][index[0]]));// energy[y][x]
                    }
                }
            }

            // Find Vertical Seam:
            double tmpSumMin = Double.POSITIVE_INFINITY;

            for(int v = 0; v < W; v++){// scan the first row
                double topdownMin = Double.POSITIVE_INFINITY;
                int w = W * (H - 1);// last row
                int index = 0;// for seam
                DijkstraSP dijkSP = new DijkstraSP(ewd, v);

                for(int lastrow = W * (H - 1); lastrow <= W * H - 1; lastrow ++){

                    if(topdownMin > dijkSP.distTo(lastrow)){
                        topdownMin = dijkSP.distTo(lastrow);
                        w = lastrow;
                    }

                }

                if(tmpSumMin > topdownMin){
                    tmpSumMin = topdownMin;
                    for(DirectedEdge de : dijkSP.pathTo(w)){// from v to w(last row)
                        seam[index++] = Number2Index(de.from(), W)[0]; // store the x
                        if(index == H - 1){
                            seam[index] = Number2Index(de.to(), W)[0]; // store the last one
                        }
                    }
                }
            }
        }else{
            // store y
            for(int v = 0; v < ewd.V(); v++){
                for(int w : neighborOfV(v, W, H)){
                    if(w != -1){
                        int[] index = Number2Index(w, H);
                        ewd.addEdge(new DirectedEdge(v, w, energyTrans[index[1]][index[0]]));// energy[y][x]
                    }
                }
            }

            // Find Horizontal Seam:
            double tmpSumMin = Double.POSITIVE_INFINITY;

            for(int v = 0; v < H; v++){// scan the first row
                double topdownMin = Double.POSITIVE_INFINITY;
                int w = H * (W - 1);// last row
                int index = 0;// for seam
                DijkstraSP dijkSP = new DijkstraSP(ewd, v);

                for(int lastrow = H * (W - 1); lastrow <= W * H - 1; lastrow ++){

                    if(topdownMin > dijkSP.distTo(lastrow)){
                        topdownMin = dijkSP.distTo(lastrow);
                        w = lastrow;
                    }

                }

                if(tmpSumMin > topdownMin){
                    tmpSumMin = topdownMin;
                    for(DirectedEdge de : dijkSP.pathTo(w)){// from v to w(last row)
                        seam[index++] = Number2Index(de.from(), H)[0]; // store the x
                        if(index == W - 1){
                            seam[index] = Number2Index(de.to(), H)[0]; // store the last one
                        }
                    }
                }
            }

        }
        return seam;
    }

//    private int Index2Number(int x, int y){
//        // energy[y][x] = pixel(x, y) = y * Width() + x = V ∈ [0, H*W - 1]
//        return y * this.width() + x;
//    }

    private int[] Number2Index(int v, int W){
        int[] ans = new int[2]; // 0 is x; 1 is y;

        ans[0] = v % W; // x = v % W;
        ans[1] = v / W; // y = (v - x) / W;

        return ans;
    }

    private int[] neighborOfV(int v, int H, int W){
        int[] neighbors = new int[3];
        neighbors[0] = -1;
        neighbors[1] = -1;
        neighbors[2] = -1;

        if(v >= W * (H - 1) && v <= W * H - 1){
            // the last row has no neighbors
            return neighbors;
        }

        if(W == 1){
            // only one direction:

            neighbors[1] = v + 1;

        }else if(v % W == 0){
            // left boundary
            // only two directions:

            neighbors[1] = v + W;
            neighbors[2] = v + W + 1;
        }else if(v % W == W - 1){
            // right boundary
            // only two directions:
            neighbors[0] = v + W - 1;
            neighbors[1] = v + W;

        }else{
            //three directions:
            neighbors[0] = v + W - 1;
            neighbors[1] = v + W;
            neighbors[2] = v + W + 1;
        }

        return neighbors;
    }

    // Topological Sort : obviously we know the order at the beginning
    // from top to bottom is that order



    // current picture
    public Picture picture(){
        return new Picture(this.picture);
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
        this.horizontalSeam = SP(W, H, false);
        return this.horizontalSeam;
    }

    // sequence of indices for vertical seam
    // The findVerticalSeam() method returns an array of length H such that entry y is the column number of the pixel
    // to be removed from row y of the image.
    public int[] findVerticalSeam(){
        this.verticalSeam = SP(W, H, true);
        return this.verticalSeam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam){
        validate(seam, false);
        // pic and energy should be changed
        Picture newPic = new Picture(W, H - 1);
        double[][] newEnergy = new double[H - 1][W];

        // new Picture: W * (H - 1)
        for(int y = 0; y < H; y++){
            for(int x = 0; x < W; x++){
                if(y == seam[x]){
                    while(y < H - 1){
                        newPic.setRGB(x, y, this.picture.getRGB(x, y + 1));
                        newEnergy[y][x] = this.energy[y + 1][x];
                        y++;
                    }
                }else{
                    newPic.setRGB(x, y, this.picture.getRGB(x, y));
                    newEnergy[y][x] = this.energy[y][x];
                }
            }
        }

        this.picture = newPic;
        this.H = this.picture.height();
        this.W = this.picture.width();
        this.energy = newEnergy;
        this.energyTrans = TransEnergy(this.energy);
        this.verticalSeam = SP(W, H, true);
        this.horizontalSeam = SP(W, H, false);

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam){
        validate(seam, true);
        Picture newPic = new Picture(W - 1, H);
        double[][] newEnergy = new double[H][W - 1];
        // new Picture: (W - 1) * H
        for(int y = 0; y < H; y++){
            for(int x = 0; x < W; x++){
                if(x == seam[y]){
                    while(x < W - 1){
                        newPic.setRGB(x, y, this.picture.getRGB(x + 1, y));
                        newEnergy[y][x] = this.energy[y][x + 1];
                        x++;
                    }
                }else{
                    newPic.setRGB(x, y, this.picture.getRGB(x, y));
                    newEnergy[y][x] = this.energy[y][x];
                }
            }
        }

        this.picture = newPic;
        this.H = this.picture.height();
        this.W = this.picture.width();
        this.energy = newEnergy;
        this.energyTrans = TransEnergy(this.energy);
        this.verticalSeam = SP(W, H, true);
        this.horizontalSeam = SP(W, H, false);
    }

    //  unit testing (optional)
    public static void main(String[] args){

    }

}