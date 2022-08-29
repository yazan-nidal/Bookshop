package exp.exalt.bookshop.constants;

import java.text.SimpleDateFormat;

public class ConstVar {
    public static final String CONTACT_ADMIN= " plz contact admin.";
    public static final String NAME_IS_NULL = "Name is null";
    public static final String FOR_EACH_ACTION = " the specified action is null ";

    ////////////////////////////
    public static final String BAD_REQUEST =  "your request fault, please contact with admin to help you skip problem.";
    public static final String SERVER_ERROR =  "its Some problem in server please, tell the admin.";
    public static final String UNAUTHORIZED_OPERATION =  "You Are Not Authorized to do this operation";
    public static final String BAD_CREDENTIAL =  "the wrong credential please re-authenticate";
    public static final String USERNAME_CONFLICT = "User (USERNAME) is used please use another";
    public static final String ASSIGN_ID_ERROR = "you cannot assign your id, it will got  it from system, if want to change it please tell admin";

    public static final String USER_DEACTIVATED = "this user deactivated, please tell admin to enable user";
    public static final String USER_CONFLICT = "User have unique property used please use another";
    public static final String USER_ID_CHANGE_ERROR = "Cannot change (id) this may cause a problem in the future, contact with admin to change it safely";
    public static final String USER_CHANGE_ROLE_PROBLEM = "can not change to this role because it will make problem to your account, please contact admin to change it safely";

    public static final String AUTHOR_ROLE_BAD_REQUEST =  "BAD_REQUEST : please Set the Role right value";
    public static final String AUTHOR_NULL_BAD_REQUEST =  "BAD_REQUEST : please check the author fields maybe you inserted invalid data like empty username or  zero Id ";
    public static final String AUTHOR_NULL =  "please check the author fields maybe you inserted invalid data like empty username or  zero Id ";
    public static final String AUTHOR_NOT_EXISTS  = "author not exists";
    public static final String AUTHOR_ID_IS_NULL =  "The Author ID is null";
    public static final String AUTHOR_USERNAME_IS_NULL =  "The Author UserName is null";
    public static final String AUTHOR_BOOK_NULL =  "please check the author book fields maybe you inserted invalid data or  empty  data";
    public static final String AUTHOR_EMPTY_BOOK_LIST = "Author does not have any book.";

    public static final String BOOK_EXISTS_BAD_REQUEST =  "BAD REQUEST YOU TRY TO ADD Book Exists ";
    public static final String BOOK_EXISTS =  "YOU TRY TO ADD Book Exists ";
    public static final String BOOK_LIST_PROBLEM = "There is a problem with the Book list or the added class, contact admin to solve it.";
    public static final String BOOK_NOT_FOUND = "Book not Found";
    public static final String BOOKS_REMOVE_PROBLEM = "Books cannot be removed, contact admin to solve it.";
    public static final String FORCE_RENT_BOOK = "can not force customer to rent book";
    public static final String FORCE_RENT_BOOKS = "can not force customer to rent books";
    public static final String BOOK_ID_CHANGE_ERROR = "Cannot change (id) this may cause a problem in the future, contact with admin to change it safely";
    public static final String BOOK_AUTHOR_CHANGE_ERROR = "cannot change the author of the book please talk to admin to change it";
    public static final String BOOK_CUSTOMER_CHANGE_ERROR = "cannot change the author of the book please talk to admin to change it";
    public static final String BOOK_NULL =  "please check the book fields maybe you inserted invalid data like empty username or  zero Id ";

    public static final String MODEL_MAPPER_ILLEGAL_EXCEPTION = "ModelMapper Illegal(Argument | Mapping | Configuration)";

    public static final String LOG_INSERT = "INSERT INTO LOGS VALUES('%s','%s','%s','%s','%s')";
    public  static  final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static final int PASSWORD_LENGTH = 5;
    public static final int USERNAME_LENGTH = 3;
    public static final String PASSWORD_LENGTH_PROBLEM = "please set password with "+PASSWORD_LENGTH+ " character at least";
    public static final String USERNAME_LENGTH_PROBLEM = "please set password with "+USERNAME_LENGTH+ " character at least";

    public static final String AUTHOR_DEL_MODE = "AUTHOR_DEL_MODE_0X";
    public static final String BOOK_DEL_MODE = "BOOK_DEL_MODE_0X";

    private ConstVar(){

    }
}

