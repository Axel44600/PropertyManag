function extractIdAppart(value) {
    return Number(value.replace(/[^\d]/g, ""));
}



// INIT
window.addEventListener('load', function() {

    // HEADER
    async function loadHeader() {
        await fetch("/app/header").then(res => res.text()).then(data => {
            document.getElementById("header").innerHTML = data;
            document.getElementById("head-appart").setAttribute("class", "active");
        }).then(() => {
    })}

    loadHeader();
    reload();
});


function reload() {
    // LOYERS
    async function loadLoyers() {
        await fetch("/app/appart/loyer/data/listOfLoyers/"+extractIdAppart(window.location.href)).then(res => res.text()).then(data => {
            document.getElementById("data").innerHTML = data;
        }).then(() => {
    })}
    loadLoyers();
}



// RESET MODULE { RECHERCHE LOYER }
document.getElementById("rchLoyer").addEventListener("click", function() {
    document.getElementById("about").style.display = "none";
    document.querySelector("input[name='dateL']").value = "";
}, false);



// RESET MODULE { AJOUTER UN LOYER }
document.getElementById("close").addEventListener("click", function() {
    document.getElementById("error").innerText = "";
    document.querySelector("input[name='date']").value = "";
}, false);



// RESET MODULE { GENERER QUITTANCE DE LOYER }
document.getElementById("createQuit").addEventListener("click", function() {
    document.getElementById("alert").style.display = "none";
    document.getElementById("alert").querySelector("p").innerText = "";
    document.getElementById("alert").querySelector("a").href = "";
    document.querySelector("input[name='dateD']").value = "";
    document.querySelector("input[name='dateF']").value = "";
}, false);



// AJOUTER UN LOYER
function createLoyer(e) {
    e.preventDefault();
    const values = new FormData(document.getElementById("createL"));
    const result = document.getElementById("error");

    fetch("/app/appart/loyer/createLoyer", {
        method: "POST",
        url : "/app/appart/loyer/createLoyer",
        body: values
    }).then((response) => {
        return response.json();
    }).then((data) => {
        if(data.nochange) {
            //
        } else {
            if (data.success == "yes") {
                result.innerText = "L'appartement a été ajouter avec succès.";
                result.style.color = "green";
                reload();
                setTimeout(function() { result.innerText = ""; }, 5000);
                document.querySelector("input[name='date']").value = "";
            } else if (data.error == "one") {
                result.innerText = "Un loyer a déjà été enregistrer pour ce mois.";
                result.style.color = "red";
            } else if (data.error == "two") {
                result.innerText = "La date du loyer ne peut pas être plus ancienne que l'appartement";
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
addEventListenerCreateLoyer();



// RECHERCHE LOYER
function researchLoyer(e) {
    e.preventDefault();
    const values = new FormData(document.getElementById("researchLoyer"));

    fetch("/app/appart/loyer/researchLoyer", {
        method: "POST",
        url : "/app/appart/loyer/researchLoyer",
        body: values
    }).then((response) => {
        return response.json();
    }).then((data) => {
        if(data.success == "yes") {
            if(data.statut == "paid") {
                document.getElementById("lStatut").innerText = "Payé";
                document.getElementById("trLoyer").style.backgroundColor = "#4caf50";
                document.getElementById("trLoyer").style.color = "#fff";
            } else {
                document.getElementById("lStatut").innerText = "Impayé";
                document.getElementById("trLoyer").style.backgroundColor = "#fa626b";
                document.getElementById("trLoyer").style.color = "#fff";
            }
            document.getElementById("about").style.display = "block";
            document.getElementById("lDate").innerText = data.date;
            document.getElementById("lRef").innerText = data.ref;
            document.getElementById("lMontant").innerText = data.montant+"€";
            document.getElementById("urlEdit").href = data.urlEdit;
            document.getElementById("idLoyerS").value = data.idLoyer;
        } else {
            document.getElementById("about").style.display = "none";
        }
    }).catch(error => {
        console.log('Erreur : ' + error.message);
    })
}
addEventListenerResearchLoyer();



// GENERER QUITTANCE DE LOYER
function createQuittance(e) {
    e.preventDefault();
    const values = new FormData(document.getElementById("createQuittance"));
    const result = document.getElementById("alert");

    fetch("/app/appart/loyer/createQuittance", {
        method: "POST",
        url : "/app/appart/loyer/createQuittance",
        body: values
    }).then((response) => {
        return response.json();
    }).then((data) => {
        if(data.success == "yes") {
            result.style.display = "block";
            result.querySelector("p").innerText = "La quittance de loyer a été générer avec succès.";
            result.querySelector("p").style.color = "green";
            result.querySelector("a").href = data.urlWeb;
            document.querySelector("input[name='dateD']").value = "";
            document.querySelector("input[name='dateF']").value = "";
        } else {
            result.querySelector("p").innerText = "Une erreur est survenue pendant l'opération.";
            result.querySelector("p").style.color = "red";
        }
    }).catch(error => {
        console.log('Erreur : ' + error.message);
    })
}
addEventListenerCreateQuittance();



// EVENTS
function addEventListenerCreateLoyer() {
    const form = document.getElementById("createL");
    form.addEventListener("submit", createLoyer, false);
}
function addEventListenerResearchLoyer() {
    const form = document.getElementById("researchLoyer");
    form.addEventListener("submit", researchLoyer, false);
}
function addEventListenerCreateQuittance() {
    const form = document.getElementById("createQuittance");
    form.addEventListener("submit", createQuittance, false);
}