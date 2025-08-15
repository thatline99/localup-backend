package thatline.localup.user.exception

import thatline.localup.common.exception.BaseException

class InvalidBusinessCustomerSegmentCodeException(
    cause: Throwable? = null,
) : BaseException("INVALID_BUSINESS_CUSTOMER_SEGMENT_CODE", cause)
