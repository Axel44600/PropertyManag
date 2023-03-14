// INIT
window.addEventListener('load', function() {
    // HEADER
    async function loadHeader() {
        await fetch("/app/header").then(res => res.text()).then(data => {
            document.getElementById("header").innerHTML = data;
            document.getElementById("head-appart").setAttribute("class", "active");
        }).then(() => {
        })
    }

    loadHeader();
});



// EDIT INFOS ETAT DES LIEUX
function editEtat(e) {
    e.preventDefault();
    const values = new FormData(document.getElementById("editEtat"));
    const result = document.getElementById("alert");

    fetch("/app/appart/etat/editEtat", {
        method: "POST",
        url : "/app/appart/etat/editEtat",
        body: values
    }).then((response) => {
        return response.json();
    }).then((data) => {
        if(data.nochange) {
            //
        } else {
            if (data.success == "yes") {
                result.innerText = "Les informations de l'état des lieux ont été modifier avec succès.";
                result.style.color = "green";
                setTimeout(function () {
                    result.innerText = "";
                }, 5000);
            } else {
                result.innerText = data.msgError;
                result.style.color = "red";
            }
        }
    }).catch(error => {
        console.log('Erreur : ' + error.message);
    })
}
addEventListenerEditEtat();



// EVENTS
function addEventListenerEditEtat() {
    const form = document.getElementById("editEtat");
    form.addEventListener("submit", editEtat, false);
}