package com.xep.thutiendien

import java.util.*

object Constrains {
    val BASE_URL = "http://apithutiendien.lamphanmem.com/"
}
 fun removeAccents(value: String?): String? {
    var MAP_NORM: Map<Char, Char>? = null
    if (MAP_NORM == null || MAP_NORM.isEmpty()) {
        MAP_NORM = HashMap<Char, Char>()
        MAP_NORM.put('À', 'A')
        MAP_NORM.put('Á', 'A')
        MAP_NORM.put('Ã', 'A')
        MAP_NORM.put('Ả', 'A')
        MAP_NORM.put('Ạ', 'A')
        MAP_NORM.put('Ă', 'A')
        MAP_NORM.put('Ắ', 'A')
        MAP_NORM.put('Ằ', 'A')
        MAP_NORM.put('Ẵ', 'A')
        MAP_NORM.put('Ẳ', 'A')
        MAP_NORM.put('Ặ', 'A')
        MAP_NORM.put('Â', 'A')
        MAP_NORM.put('Ầ', 'A')
        MAP_NORM.put('Ấ', 'A')
        MAP_NORM.put('Ẫ', 'A')
        MAP_NORM.put('Ẩ', 'A')
        MAP_NORM.put('Ậ', 'A')
        MAP_NORM.put('È', 'E')
        MAP_NORM.put('É', 'E')
        MAP_NORM.put('Ẽ', 'E')
        MAP_NORM.put('Ẻ', 'E')
        MAP_NORM.put('Ẹ', 'E')
        MAP_NORM.put('Ê', 'E')
        MAP_NORM.put('Ế', 'E')
        MAP_NORM.put('Ề', 'E')
        MAP_NORM.put('Ễ', 'E')
        MAP_NORM.put('Ể', 'E')
        MAP_NORM.put('Ệ', 'E')
        MAP_NORM.put('Í', 'I')
        MAP_NORM.put('Ì', 'I')
        MAP_NORM.put('Ĩ', 'I')
        MAP_NORM.put('Ỉ', 'I')
        MAP_NORM.put('Ị', 'I')
        MAP_NORM.put('Ù', 'U')
        MAP_NORM.put('Ú', 'U')
        MAP_NORM.put('Ũ', 'U')
        MAP_NORM.put('Ủ', 'U')
        MAP_NORM.put('Ụ', 'U')
        MAP_NORM.put('Ư', 'U')
        MAP_NORM.put('Ừ', 'U')
        MAP_NORM.put('Ứ', 'U')
        MAP_NORM.put('Ữ', 'U')
        MAP_NORM.put('Ử', 'U')
        MAP_NORM.put('Ự', 'U')
        MAP_NORM.put('Ò', 'O')
        MAP_NORM.put('Ó', 'O')
        MAP_NORM.put('Õ', 'O')
        MAP_NORM.put('Ỏ', 'O')
        MAP_NORM.put('Ọ', 'O')
        MAP_NORM.put('Ô', 'O')
        MAP_NORM.put('Ố', 'O')
        MAP_NORM.put('Ồ', 'O')
        MAP_NORM.put('Ỗ', 'O')
        MAP_NORM.put('Ổ', 'O')
        MAP_NORM.put('Ộ', 'O')
        MAP_NORM.put('Ơ', 'O')
        MAP_NORM.put('Ớ', 'O')
        MAP_NORM.put('Ờ', 'O')
        MAP_NORM.put('Ỡ', 'O')
        MAP_NORM.put('Ở', 'O')
        MAP_NORM.put('Ợ', 'O')
        MAP_NORM.put('à', 'a')
        MAP_NORM.put('á', 'a')
        MAP_NORM.put('ã', 'a')
        MAP_NORM.put('ả', 'a')
        MAP_NORM.put('ạ', 'a')
        MAP_NORM.put('ă', 'a')
        MAP_NORM.put('ắ', 'a')
        MAP_NORM.put('ằ', 'a')
        MAP_NORM.put('ẵ', 'a')
        MAP_NORM.put('ẳ', 'a')
        MAP_NORM.put('ặ', 'a')
        MAP_NORM.put('â', 'a')
        MAP_NORM.put('ầ', 'a')
        MAP_NORM.put('ấ', 'a')
        MAP_NORM.put('ẫ', 'a')
        MAP_NORM.put('ẩ', 'a')
        MAP_NORM.put('ậ', 'a')
        MAP_NORM.put('è', 'e')
        MAP_NORM.put('é', 'e')
        MAP_NORM.put('ẽ', 'e')
        MAP_NORM.put('ẻ', 'e')
        MAP_NORM.put('ẹ', 'e')
        MAP_NORM.put('ê', 'e')
        MAP_NORM.put('ế', 'e')
        MAP_NORM.put('ề', 'e')
        MAP_NORM.put('ễ', 'e')
        MAP_NORM.put('ể', 'e')
        MAP_NORM.put('ệ', 'e')
        MAP_NORM.put('í', 'i')
        MAP_NORM.put('ì', 'i')
        MAP_NORM.put('ĩ', 'i')
        MAP_NORM.put('ỉ', 'i')
        MAP_NORM.put('ị', 'i')
        MAP_NORM.put('ù', 'u')
        MAP_NORM.put('ú', 'u')
        MAP_NORM.put('ũ', 'u')
        MAP_NORM.put('ủ', 'u')
        MAP_NORM.put('ụ', 'u')
        MAP_NORM.put('ư', 'u')
        MAP_NORM.put('ừ', 'u')
        MAP_NORM.put('ứ', 'u')
        MAP_NORM.put('ữ', 'u')
        MAP_NORM.put('ử', 'u')
        MAP_NORM.put('ự', 'u')
        MAP_NORM.put('ò', 'o')
        MAP_NORM.put('ó', 'o')
        MAP_NORM.put('õ', 'o')
        MAP_NORM.put('ỏ', 'o')
        MAP_NORM.put('ọ', 'o')
        MAP_NORM.put('ô', 'o')
        MAP_NORM.put('ồ', 'o')
        MAP_NORM.put('ố', 'o')
        MAP_NORM.put('ỗ', 'o')
        MAP_NORM.put('ổ', 'o')
        MAP_NORM.put('ộ', 'o')
        MAP_NORM.put('ơ', 'o')
        MAP_NORM.put('ờ', 'o')
        MAP_NORM.put('ớ', 'o')
        MAP_NORM.put('ỡ', 'o')
        MAP_NORM.put('ở', 'o')
        MAP_NORM.put('ợ', 'o')
        MAP_NORM.put('Đ', 'd')
        MAP_NORM.put('đ', 'd')
        MAP_NORM.put('ỷ', 'y')
        MAP_NORM.put('ý', 'y')
        MAP_NORM.put('ỳ', 'y')
        MAP_NORM.put('Ỷ', 'Y')
        MAP_NORM.put('Ý', 'Y')
        MAP_NORM.put('Ỳ', 'Y')
    }
    if (value == null) {
        return ""
    }
    val sb = StringBuilder(value)
    for (i in 0 until value.length) {
        val c: Char? = MAP_NORM.get(sb[i])
        if (c != null) {
            sb.setCharAt(i, c)
        }
    }
    return sb.toString()
}
