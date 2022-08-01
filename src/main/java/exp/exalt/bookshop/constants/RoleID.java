package exp.exalt.bookshop.constants;

public enum RoleID{
    AUTHOR,CUSTOMER,ADMIN;
    public String getRole() {
        switch(this) {
            case ADMIN:
                return "ADMIN";
            case AUTHOR:
                return "AUTHOR";
            case CUSTOMER:
                return "CUSTOMER";
            default:
                return "-1";
        }
    }
}
