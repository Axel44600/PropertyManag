function alertMsg() {
    const str = window.location.href;
    const url = new URL(str);
    const search_params = new URLSearchParams(url.search);

    if(search_params.has('error')) {
        const msg = document.getElementById("error");
        msg.innerHTML = "<b style='color: red;'>Pseudonyme ou mot de passe incorrect.</b>";
        msg.style.display = "block";
    }
}