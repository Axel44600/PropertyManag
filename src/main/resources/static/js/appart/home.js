const url = document.location.href;

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

    loadHeader().then(r => r);
    if(url.includes("appart") && !url.includes("etat") && !url.includes("depot")) {
        reload();
    }
});


if(url.includes("appart") && !url.includes("etat") && !url.includes("depot")) {

    function reload() {
        // APPARTEMENTS
        async function loadApparts() {
            await fetch("/app/data/listOfApparts").then(res => res.text()).then(data => {
                document.getElementById("data").innerHTML = data;
            }).then(() => {
            })
        }

        loadApparts().then(r => r);
    }


    // RESET MODULE { RECHERCHE APPARTEMENT }
    document.getElementById("rchAppart").addEventListener("click", function () {
        document.getElementById("about").style.display = "none";
        document.querySelector("input[name='address']").value = "";
    }, false);


    // RESET MODULE { AJOUTER UN APPARTEMENT }
    document.getElementById("close").addEventListener("click", function () {
        document.getElementById("error").innerText = "";
        document.querySelector("input[name='adressForm']").value = "";
        document.querySelector("input[name='adressCompForm']").value = "";
        document.querySelector("input[name='villeForm']").value = "";
        document.querySelector("input[name='cPostalForm']").value = "";
        document.querySelector("input[name='loyerForm']").value = "";
        document.querySelector("input[name='chargesForm']").value = "";
        document.querySelector("input[name='depotGForm']").value = "";
        document.querySelector("input[name='dateForm']").value = "";
    }, false);


    // CREATE APPARTEMENT
    function createAppart(e) {
        e.preventDefault();
        const values = new FormData(document.getElementById("createA"));
        const result = document.getElementById("error");

        fetch("/app/createAppart", {
            method: "POST",
            url: "/app/createAppart",
            body: values
        }).then((response) => {
            return response.json();
        }).then((data) => {
            if (data.success === "yes") {
                result.innerText = "L'appartement a été ajouter avec succès.";
                result.style.color = "green";
                setTimeout(function () {
                    result.innerText = "";
                }, 5000);
                document.querySelector("input[name='adressForm']").value = "";
                document.querySelector("input[name='adressCompForm']").value = "";
                document.querySelector("input[name='villeForm']").value = "";
                document.querySelector("input[name='cPostalForm']").value = "";
                document.querySelector("input[name='loyerForm']").value = "";
                document.querySelector("input[name='chargesForm']").value = "";
                document.querySelector("input[name='depotGForm']").value = "";
                document.querySelector("input[name='dateForm']").value = "";
                reload();
            } else if (data.error === "one") {
                result.innerText = data.msgError;
                result.style.color = "red";
            } else if (data.error === "two") {
                result.innerText = "Il existe déjà un appartement enregistré à cette adresse.";
                result.style.color = "red";
            }
        }).catch(error => {
            console.log('Erreur : ' + error.message);
        })
    }

    addEventListenerCreateAppart();


    // RECHERCHE APPARTEMENT
    function researchAppart(e) {
        e.preventDefault();
        const values = new FormData(document.getElementById("researchAppart"));

        fetch("/app/researchAppart", {
            method: "POST",
            url: "/app/researchAppart",
            body: values
        }).then((response) => {
            return response.json();
        }).then((data) => {
            if (data.success === "yes") {
                document.getElementById("about").style.display = "block";
                document.getElementById("id").innerText = data.id;
                document.getElementById("idAppart").innerText = data.id;
                document.querySelector("input[name='idAppart']").value = data.id;
                document.getElementById("adresse").innerText = data.adresse;
                document.getElementById("urlActions").setAttribute("data-bs-target", "#" + data.urlActions);
                document.querySelector(".modal.fade1").setAttribute("id", data.urlActions);
                document.getElementById("urlEdit").href = data.urlEdit;
                document.getElementById("urlSeeLoyer").href = data.urlSeeLoyer;
                document.getElementById("urlSeeEtat").href = data.urlSeeEtat;
                document.getElementById("urlSeeDepot").href = data.urlSeeDepot;
            } else {
                document.getElementById("about").style.display = "none";
            }
        }).catch(error => {
            console.log('Erreur : ' + error.message);
        })
    }

    addEventListenerResearchAppart();


    // EVENTS
    function addEventListenerCreateAppart() {
        const form = document.getElementById("createA");
        form.addEventListener("submit", createAppart, false);
    }
    function addEventListenerResearchAppart() {
        const form = document.getElementById("researchAppart");
        form.addEventListener("submit", researchAppart, false);
    }

}