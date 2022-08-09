package exp.exalt.bookshop.models;

public enum RoleID{
    ADMIN,AUTHOR,CUSTOMER;
    public int getRole() {
        switch(this) {
            case ADMIN:
                return 0;
            case AUTHOR:
                return 1;
            case CUSTOMER:
                return 2;
            default:
                return Integer.MIN_VALUE;
        }
    }

    public static String getRole(int i){
        switch(i) {
            case 1:
                return "ADMIN";
            case 2:
                return "AUTHOR";
            case 3:
                return "CUSTOMER";
            default:
                return "None";
        }
    }
}
