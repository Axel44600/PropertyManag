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



// EDIT LOYER
function editLoyer(e) {
    e.preventDefault();
    const values = new FormData(document.getElementById("editLoyer"));
    const result = document.getElementById("alert");

    fetch("/app/appart/loyer/editLoyer", {
        method: "POST",
        url : "/app/appart/loyer/editLoyer",
        body: values
    }).then((response) => {
        return response.json();
    }).then((data) => {
        if(data.nochange) {
            //
        } else {
            if (data.success == "yes") {
                result.innerText = "Le loyer a été modifier avec succès.";
                result.style.color = "green";
                setTimeout(function () {
                    result.innerText = "";
                }, 5000);
            } else if (data.error == "one") {
                result.innerText = "Un loyer a déjà été enregistrer pour ce mois.";
                result.style.color = "red";
            } else if (data.error == "two") {
                result.innerText = "La date du loyer ne peut pas être plus ancienne que l'appartement.";
                result.style.color = "red";
            } else {
                result.innerText = data.msgError;
                result.style.color = "red";
            }
        }
    }).catch(error => {
        console.log('Erreur : ' + error.message);
    })
}
addEventListenerEditLoyer();



// EVENTS
function addEventListenerEditLoyer() {
    const form = document.getElementById("editLoyer");
    form.addEventListener("submit", editLoyer, false);
}