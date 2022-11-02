class Department implements Comparable<Department>{
    private String depName;

    public static GenericLinkedList<Department> depLL = new GenericLinkedList<>();

    public Department(String depName) {
        this.depName = depName;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }
    public void add(Department o){
        depLL.add(o);
    }
    public GenericLinkedList getList(){
        return depLL;
    }
    @Override
    public int compareTo (Department o) {
        int compareResult = this.depName.compareTo(o.depName);
        if (compareResult < 0){
            return -1;
        } else if (compareResult > 0){
            return 1;
        } else
            return 0;
    }
}
