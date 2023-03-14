function Redirect() {
    window.location.replace("/auth");
}



// FIRST LOGIN
function firstLogin(e) {
    e.preventDefault();
    const values = new FormData(document.getElementById("formFirst"));
    const result = document.getElementById("alert");

    fetch("first_auth", {
        method: "POST",
        url : "first_auth",
        body: values
    }).then((response) => {
        return response.json();
    }).then((data) => {
        if(data.success == "yes") {
            result.innerText = "Votre compte est désormais activé, redirection dans 5s...";
            result.style.color = "green";
            setTimeout('Redirect()', 5000);
            document.querySelector("input[name='registerKey']").value = "";
            document.querySelector("input[name='pseudo']").value = "";
            document.querySelector("input[name='password']").value = "";
            document.querySelector("input[name='repassword']").value = "";
        } else if(data.error == "one") {
            result.innerText = "Votre clé d'enregistrement n'est pas valide.";
            result.style.color = "red";
        } else if(data.error == "two") {
            result.innerText = "Ce pseudonyme est déjà utilisé.";
            result.style.color = "red";
        } else if(data.error == "three") {
            result.innerText = "Les mots de passe ne correspondent pas.";
            result.style.color = "red";
        } else {
            result.innerText = data.msgError;
            result.style.color = "red";
        }
    }).catch(error => {
        console.log('Erreur : ' + error.message);
    })
}
addEventListenerFirstLogin();



// EVENTS
function addEventListenerFirstLogin() {
    const form = document.getElementById("formFirst");
    form.addEventListener("submit", firstLogin, false);
}