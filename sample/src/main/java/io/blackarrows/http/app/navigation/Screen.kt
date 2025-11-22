package io.blackarrows.http.app.navigation

sealed class Screen(val route: String, val title: String) {
    object Menu : Screen("menu", "HTTP Library Tests")
    object GetJson : Screen("get_json", "GET JSON (Images)")
    object PostJson : Screen("post_json", "POST JSON")
    object GetRaw : Screen("get_raw", "GET Raw/Binary Data")
    object PutJson : Screen("put_json", "PUT JSON")
    object DeleteJson : Screen("delete_json", "DELETE JSON")
    object PostForm : Screen("post_form", "POST Form Data")
}
