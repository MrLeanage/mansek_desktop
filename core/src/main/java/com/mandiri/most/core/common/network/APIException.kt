package com.mandiri.most.core.common.network

import java.io.IOException

class APIException(val error: APIError, message: String?): IOException(message)