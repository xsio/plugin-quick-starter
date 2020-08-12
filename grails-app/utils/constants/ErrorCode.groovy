package constants

class ErrorCode {

    static final def GENERAL_INTERNAL_ERROR = 500000

    static final def GENERAL_ERROR = 400000
    static final def VALIDATION_ERROR = 400001
    static final def DATA_NOT_FOUND = 400002
    static final def DUPLICATED_KEY = 400003
    static final def CANNOT_UPDATE = 400004
    static final def MISSING_FIELD = 400005
    static final def WRONG_TYPE = 400006
    static final def OPTIMISTIC_LOCK = 400007
    static final def UNAUTHORIZED = 400008
    static final def ACCESS_DENIED = 400009

    static final def FILE_TOO_LARGE = 400100
    static final def FIELD_TOO_LONG = 400101
    static final def INVALID_DEADLINE = 400102
    static final def FLOWER_NOT_ENOUGH = 400103
    static final def AUTHORITY_NOT_ENOUGH = 400104

}
