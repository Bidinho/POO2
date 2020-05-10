import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/***
 * SparseMatrix is the data structure that only stores the non-zero values, saving a lot of memory.
 * It implements the Interface IMatrix and Cloneable.
 * It has the following data members:
 * - matrix, which is a SentinelLL (Sentinel Node Linked List)
 * - endRows, which is an int that represents where the sparse matrix finish ("row wise").
 * - endColumns,  which is an int that represents where the sparse matrix finish ("column wise")
 * - startingRow, which is an int that represents where the sparse matrix starts ("row wise")
 * - startingColumn, which is an int that represents where the sparse matrix starts ("column wise")
 *
 * @param <L>
 */
public class SparseMatrix<L> implements IMatrix<L>, Cloneable {
    private SentinelLL matrix;
    private int endRow;
    private int endColumn;
    private int startingRow;
    private int startingColumn;

    /***
     * Constructor that it will take a array list of strings as parameter. It is used to get the input from the
     * console and create a sparse matrix from that.
     * @pre Each string of the array list must contain only "0" or "1".
     * @param input - ArrayList of strings
     * @pos new Object of SparseMatrix, this.rows and this.columns >= 0. as well this.startingRow and this
     * .startingColumn = 0.
     * @throws SentinelLLException if there is no SentinelNode at the given position.
     * @throws SparseMatrixException if there is no DataNode at the given position.
     */
    public SparseMatrix(ArrayList<String> input) throws SparseMatrixException, SentinelLLException {
        if (input.size() == 0)
            throw new SparseMatrixException("Input is empty");
        for (int i = 0; i < input.size() - 1; i++) {
            if (input.get(i).split("").length != input.get(i + 1).split("").length)
                throw new SparseMatrixException("Matrix format is incorrect");
        }
        setEndRow(input.size() - 1);
        setEndColumn(input.get(0).split("").length - 1);
        setStartingRow(0);
        setStartingColumn(0);
        setMatrix(input);
    }

    /**
     * Constructor that it will take the following parameters.
     *
     * @param startingRow
     * @param startingColumn
     * @param endRow
     * @param endColumn
     * @pre endRow and endColumn must be >= 0. startingRow <= endRow, startingColumn <= endColumn.
     * @pos new Object of SparseMatrix.
     */
    public SparseMatrix(int startingRow, int startingColumn, int endRow, int endColumn) throws SparseMatrixException, SentinelLLException {
        setEndRow(endRow);
        setEndColumn(endColumn);
        setStartingColumn(startingColumn);
        setStartingRow(startingRow);
        setMatrix();
    }

    /***
     * Setter for the matrix field. It creates a new SentinelLL, and it will add the sentinel nodes to it. For the
     * matrix we will have the max between rows and columns as our highest value sentinel node and it will start with
     * min between startingRow and startingColumn as our lowest value sentinel node.
     * @pre true
     * @pos Matrix with the sentinel nodes that range from the lowest value to the highest value.
     */
    public void setMatrix() throws SentinelLLException {
        this.matrix = new SentinelLL();
        for (int i = Math.min(this.startingRow, startingColumn); i <= Math.max(this.endRow, this.endColumn); i++) {
            this.matrix.addLast(i);
        }
    }

    /***
     * Setter for the matrix field with the parameter of an array list of strings. Here we will be expecting the
     * input from the console to be converted into an array list and then it will come as the argument for this
     * method. First of all we will be initializing the sentinel nodes, then we will be adding the data nodes where
     * it should have one, given the input. After that we should have our sparse matrix filled with data nodes.
     * @pre true
     * @param input
     * @pos Matrix field with all the initial sentinel nodes and data nodes properly set.
     * @throws SentinelLLException if there is no SentinelNode.
     */
    @SuppressWarnings("unchecked")
    public void setMatrix(ArrayList<String> input) throws SentinelLLException {
        setMatrix();
        Iterator<String> iterator = input.iterator();
        while (iterator.hasNext()) {
            for (int i = 0; i < this.endRow + 1; i++) {
                String[] line = iterator.next().split("");
                for (int j = 0; j < this.endColumn + 1; j++) {
                    if (line[j].equals("1")) {
                        setElement(i, j, (L) new LivingCell());
                    }
                }
            }
        }
    }

    /***
     * Setter for endRow field.
     * @pre endRow >= 0
     * @param endRow
     * @pos this.endRow = endRow
     * @throws SparseMatrixException if there is no DataNode at the given row.
     */
    public void setEndRow(int endRow) throws SparseMatrixException {
        if (endRow < 0) {
            throw new SparseMatrixException("endRow < 0");
        }
        this.endRow = endRow;
    }

    /***
     * Setter for endColumn field.
     * @pre endColumn >= 0
     * @param column
     * @pos this.endColumn = endColumn
     * @throws SparseMatrixException if there is no DataNode at the given column.
     */
    public void setEndColumn(int column) throws SparseMatrixException {
        if (column < 0) {
            throw new SparseMatrixException("endColumn < 0");
        }
        this.endColumn = column;
    }

    /***
     * Setter for startingRow field.
     * @pre startingRow <= endRow && startingRow >= 0
     * @param startingRow
     * @pos this.startingRow = startingRow
     * @throws SparseMatrixException if there is no DataNode at the given row.
     */
    public void setStartingRow(int startingRow) throws SparseMatrixException {
        if (startingRow > this.endRow)
            throw new SparseMatrixException("startingRow >= rows");
        this.startingRow = startingRow;
    }

    /***
     * Setter for startingColumn field.
     * @pre startingColumn <= endColumn && startingColumn >= 0
     * @param startingColumn
     * @pos this.startingColumn = startingColumn
     * @throws SparseMatrixException if there is no DataNode at the given column.
     */
    public void setStartingColumn(int startingColumn) throws SparseMatrixException {
        if (startingColumn > this.endColumn)
            throw new SparseMatrixException("startingColumn >= columns");
        this.startingColumn = startingColumn;
    }


    /***
     * Getter for matrix field.
     * @return SentinelLL matrix
     */
    public SentinelLL getMatrix() {
        return this.matrix;
    }

    /***
     * Getter for rows. It is calculated by subtracting the end row by starting row.
     * @return int that represents how many rows the sparse matrix has at a given point.
     */
    public int getRows() {
        return this.endRow - this.startingRow + 1;
    }

    /***
     * Getter for columns. It is calculated by subtracting the end column by starting column.
     * @return int that represents how many columns the sparse matrix has at a given point.
     */
    public int getColumns() {
        return this.endColumn - this.startingColumn + 1;
    }

    /***
     * Getter for the startingColumn field.
     * @return int this.startingColumn
     */
    public int getStartingColumn() {
        return startingColumn;
    }

    /***
     * Getter for the startingRow field.
     * @return int this.startingRow
     */
    public int getStartingRow() {
        return startingRow;
    }

    /***
     * Getter for the endRow field.
     * @return int this.endRow
     */
    public int getEndRow() {
        return endRow;
    }

    /**
     * Getter for the endColumn field.
     *
     * @return int this.endColumn
     */
    public int getEndColumn() {
        return endColumn;
    }


    /**
     * Setter for the position i,j on the sparse matrix. It will set the chosen position to given value. If there is
     * no DataNode on that position, it will initiate one with the given value.
     *
     * @param i     int that represents the position on the sparse matrix ("row wise")
     * @param j     int that represents the position on the sparse matrix ("column wise")
     * @param value generic type.
     * @throws SentinelLLException if there is no SentinelNode at the given position.
     * @pre true
     */
    public void setElement(int i, int j, L value) throws SentinelLLException {
        if (getDataNode(i, j) == null) {
            addDataNode(i, j, value);
        } else {
            this.getDataNode(i, j).setValue(value);
        }
    }

    /**
     * Getter for the element stored in a DataNode. If the element does not exist it will throw an excpetion.
     *
     * @param i int that represents the position on the sparse matrix ("row wise")
     * @param j int that represents the position on the sparse matrix ("column wise")
     * @return L generic type
     * @throws SentinelLLException   if there is no SentinelNode at the given position.
     * @throws SparseMatrixException if there is no DataNode at the given position.
     */
    @SuppressWarnings("unchecked")
    public L getElement(int i, int j) throws SentinelLLException, SparseMatrixException {
        DataNode<L> result = getDataNode(i, j);
        if (result == null)
            throw new SparseMatrixException("Trying to get element, element does not exist");
        return result.getValue();
    }

    /***
     * Getter for the DataNode. Given an i,j position for the sparse matrix it will return a DataNode if there is one
     * . The algorithm is the following:
     * - Get a SentinelNode that correspond to the current row.
     * - Get a SentinelNode that correspond to the current row that will act as a token.
     * - We will presume there is no data node on the given position. That means we will be setting the result to null.
     * - Loops start till the end of the row, meaning the node will point back to the currentRow sentinel node.
     * - Create a DataNode (east of the previously one) and check if is on the same column as the given argument.
     * - If it is, that is our DataNode, otherwise we change our token and keep looking east.
     * - If we get in the end and havent found any DataNode the result is null.
     * @param i
     * @param j
     * @return
     * @throws SentinelLLException if there is no SentinelNode at the given position.
     */
    @SuppressWarnings("unchecked")
    public DataNode<L> getDataNode(int i, int j) throws SentinelLLException {
        if (!matrix.contains(i)) {
            throw new SentinelLLException("Sentinel Node does not exist!");
        }
        Node currentRow = this.matrix.getNode(i);
        Node token = this.matrix.getNode(i);
        DataNode<LivingCell> result = null;
        while (token.getEast() != currentRow) {
            DataNode<LivingCell> data = (DataNode<LivingCell>) token.getEast();
            if (data.getJ() == j) {
                result = data;
                break;
            }
            token = token.getEast();
        }
        return (DataNode<L>) result;
    }

    /***
     * Getter for the whole chosen column. It will return an array list of DataNode.
     * @pre true
     * @param column int that represents which column it will return.
     * @return ArrayList that contains DataNode of the chosen column.
     * @throws SentinelLLException if there is no SentinelNode at the given column.
     */
    @SuppressWarnings("unchecked")
    public List<L> getWholeColumn(int column) throws SentinelLLException {
        ArrayList<L> result = new ArrayList<>();
        Node sentinel = matrix.getNode(column);
        Node token = sentinel;
        while (token.getSouth() != sentinel) {
            result.add((L) token.getSouth());
            token = token.getSouth();
        }
        return result;
    }

    /***
     * Getter for the whole chosen row. It will return an array list of DataNode.
     * @pre true
     * @param row int that represents which row it will return.
     * @return ArrayList that contains DataNode of the chosen row.
     * @throws SentinelLLException if there is no SentinelNode at the given row.
     */
    @SuppressWarnings("unchecked")
    public List<L> getWholeRow(int row) throws SentinelLLException {
        ArrayList<L> result = new ArrayList<>();
        Node sentinel = matrix.getNode(row);
        Node token = sentinel;
        while (token.getEast() != sentinel) {
            result.add((L) token.getEast());
            token = token.getEast();
        }
        return result;
    }


    /***
     * A helper function to the resize method. Changes the startingRow, startingColumn, endRow or endColumn if
     * necessary, so it can grow further.
     * @param i int that represents the position on the sparse matrix ("row wise")
     * @param j int that represents the position on the sparse matrix ("column wise")
     */
    private void redefineBorders(int i, int j) {
        if (i < this.startingRow) {
            this.startingRow = i;
        } else if (i > this.endRow) {
            this.endRow = i;
        }
        if (j < this.startingColumn) {
            this.startingColumn = j;
        } else if (j > this.endColumn) {
            this.endColumn = j;
        }
    }


    /**
     * A helper function to the resize method. If the sparse matrix needs to be resized not only for the outer border
     * but further, then the Sentinel Nodes needs to be added in between so is guaranteed there is no gap between one
     * sentinel node and the other.
     *
     * @param i int that represents till which position will need a sentinel node ("row wise")
     * @param j int that represents till which position will need a sentinel node ("column wise")
     * @pre true
     * @pos No gap between Sentinel Nodes.
     * @throws SentinelLLException if the size of the list is < 1 when trying to addFirst
     */

    private void addRemainingSentinels(int i, int j) {
        if (i > endRow) {
            for (int k = endRow + 1; k <= i; k++) {
                this.matrix.addLast(k);
            }
        } else if (i < startingRow) {
            for (int k = startingRow - 1; k >= i; k--) {
                this.matrix.addFirst(k);
            }
        }
        if (j > endColumn) {
            for (int k = endColumn + 1; k <= j; k++) {
                if (!this.matrix.contains(k))
                    this.matrix.addLast(k);
            }
        } else if (j < startingColumn) {
            for (int k = startingColumn - 1; k >= j; k--) {
                if (!this.matrix.contains(k))
                    this.matrix.addFirst(k);
            }
        }
    }

    /***
     * Resize for the sparse matrix. Given a new i and j, it will resize to the given arguments. It will be added
     * Sentinel Nodes, if needed. startingRow, startingColumn, endRow and endColumn will have a new value, if
     * needed as well.
     * @param i int that represents the position on the sparse matrix ("row wise")
     * @param j int that represents the position on the sparse matrix ("column wise")
     * @throws SentinelLLException if the size of the list is < 1 when trying to addFirst in addRemainingSentinels
     */
    public void resize(int i, int j) throws SentinelLLException {
        addRemainingSentinels(i, j);
        redefineBorders(i, j);
    }

    /***
     * Delete a DataNode given the position i,j of the sparse matrix.
     * Here we will change the pointers of the Nodes before and after the DataNode we want to delete, doing so, we
     * make sure the garbage collector will remove the chosen DataNode out of memory.
     * @param i int that represents the position on the sparse matrix ("row wise")
     * @param j int that represents the position on the sparse matrix ("column wise")
     * @throws SparseMatrixException if there is no DataNode at the given position.
     * @throws SentinelLLException if there is no SentinelNode at the given position.
     */
    @SuppressWarnings("unchecked")
    public void deleteDataNode(int i, int j) throws SparseMatrixException, SentinelLLException {
        DataNode<LivingCell> node = (DataNode<LivingCell>) getDataNode(i, j);
        if (node == null)
            throw new SparseMatrixException("Trying to delete, DataNode not found");
        Node currentRow = this.matrix.getNode(node.getI());
        while (currentRow.getEast() != node) {
            currentRow = currentRow.east;
        }
        currentRow.setEast(node.east);
        Node currentColumn = this.matrix.getNode(node.getJ());
        while (currentColumn.getSouth() != node) {
            currentColumn = currentColumn.south;
        }
        currentColumn.setSouth(node.south);
    }

    /***
     * Add a DataNode to the given position i,j of the sparse matrix with the given value content.
     * Here we will go through the DataNodes till the end and then add a new DataNode, therefore if there is no
     * DataNode on the same row we will add right there. Same apply for the column. East and South will be properly set
     * at the end.
     * @pre true
     * @param i int that represents the position on the sparse matrix ("row wise")
     * @param j int that represents the position on the sparse matrix ("column wise")
     * @throws SentinelLLException if there is no SentinelNode on the chosen position
     */
    @SuppressWarnings("unchecked")
    public void addDataNode(int i, int j, L content) throws SentinelLLException {
        Node row = this.matrix.getNode(i);
        if (row.getEast() == row) {
            row.setEast(new DataNode<>(null, row, i, j, content));
        } else {
            DataNode<L> data = (DataNode<L>) row.east;
            while (data.getEast() != row && data.getJ() < j) {
                data = (DataNode<L>) data.east;
            }
            data.setEast(new DataNode<>(null, data.getEast(), i, j, content));
        }
        Node column = this.matrix.getNode(j);
        if (column.south == column) {
            DataNode<L> result = getDataNode(i, j);
            column.setSouth(result);
            result.setSouth(column);
        } else {
            DataNode<L> data = (DataNode<L>) column.south;
            while (data.getSouth() != column && data.getI() < i) {
                data = (DataNode<L>) data.south;
            }
            DataNode<L> result = getDataNode(i, j);
            result.setSouth(data.getSouth());
            data.setSouth(result);
        }
    }


    /***
     * Clone for the sparse matrix data structure. It creates a deep copy of the sparse matrix that is called.
     * @return SparseMatrix object
     * @throws CloneNotSupportedException
     */
    @SuppressWarnings("unchecked")
    protected Object clone() throws CloneNotSupportedException {
        SparseMatrix<L> newSparse = (SparseMatrix<L>) super.clone();
        try {
            newSparse.setMatrix();
        } catch (SentinelLLException e) {
            e.printStackTrace();
        }
        for (int i = this.startingRow; i < this.endRow + 1; i++) {
            for (int j = this.startingColumn; j < this.endColumn + 1; j++) {
                try {
                    if (this.getDataNode(i, j) != null) {
                        newSparse.setElement(i, j, (L) new LivingCell()); //TODO
                    }
                } catch (SentinelLLException e) {
                    System.out.println(e.toString());
                }
            }
        }
        return newSparse;
    }

    /***
     * toString for the sparse matrix structure
     * @return string that represents the sparse matrix.
     */
    @Override
    @SuppressWarnings("unchecked")
    public String toString() {
        String result = "";
        for (int i = startingColumn; i < endColumn; i++) {
            Node sentinel = null;
            try {
                sentinel = matrix.getNode(i);
            } catch (SentinelLLException e) {
                e.printStackTrace();
            }
            Node token = sentinel;
            while (token.getSouth() != sentinel) {
                DataNode<L> data = (DataNode<L>) token.getSouth();
                result = result.concat(data.value.toString() + " on position " + data.getI() + "," + data.getJ() + "\n");
                token = token.getSouth();
            }
        }
        return result;
    }


    /*******************************************************
     *                                                     *
     *              The Data Node class                    *
     *                                                     *
     *******************************************************/

    /***
     * Our DataNode Class holds information such as the int "i" and "j" that represents the position of the DataNode in
     * the sparse matrix, a generic type stored in "value" and store as well two more important information, which are
     * the Node "south" and "east". For these last two data members, we have the behavior that is the following:
     *  - "are references to either the next non zero data node or to a sentinel node. The E of the last data node in a
     *  given row points back to the sentinel node of its row. Similarly, S of the last data node in a given column
     *  points back to the sentinel node of its column." - Professor Joao Valente Oliveira.
     *  Where S and E indicates "south" and "east" respectively.
     * @param <L>
     */
    private static class DataNode<L> extends Node {

        private L value;
        private int i;
        private int j;

        /***
         * Constructor for the DataNode, it takes the Node south and Node east, using the constructor of its super
         * class. Also it takes a position i,j and a value as well, and set those fields with the setters for each
         * one of them.
         * @param south Node that represents the next non-zero data ("column wise") or the sentinel node if there is
         *              none DataNode on the same column.
         * @param east Node that represents the next non-zero data ("row wise") or the sentinel node if there is
         *          *              none DataNode on the same row.
         * @param i int that represents the position on the sparse matrix ("row wise")
         * @param j int that represents the position on the sparse matrix ("column wise")
         * @param value generic type
         */
        public DataNode(Node south, Node east, int i, int j, L value) {
            super(south, east);
            setI(i);
            setJ(j);
            setValue(value);
        }

        /***
         * Setter for the i field.
         * @param i
         * @pos this.i = i
         */
        public void setI(int i) {
            this.i = i;
        }

        /***
         * Setter for the j field.
         * @param j
         * @pos this.j = j
         */
        public void setJ(int j) {
            this.j = j;
        }

        /***
         * Setter for the value field.
         * @param value
         * @pos this.value = value
         */
        public void setValue(L value) {
            this.value = value;
        }

        /***
         * Getter for the i field.
         * @return this.i
         */
        public int getI() {
            return i;
        }

        /***
         * Getter for the j field.
         * @return this.j
         */
        public int getJ() {
            return j;
        }

        /***
         * Getter for the value field.
         * @return this.value
         */
        public L getValue() {
            return value;
        }

        /***
         * Setter for the south field. Uses the super class constructor.
         * @param south
         */
        @Override
        public void setSouth(Node south) {
            super.setSouth(south);
        }

        /***
         * Setter for the east field. Uses the super class constructor.
         * @param east
         */
        @Override
        public void setEast(Node east) {
            super.setEast(east);
        }

        /***
         * toString for the DataNode structure.
         * @return string that represents a DataNode.
         */
        @Override
        public String toString() {
            return "DataNode{" +
                    "\nRow= " + (i + 1) +
                    "\nColumn= " + (j + 1) +
                    "\nvalue=" + value +
                    "\n}";
        }
    }
}