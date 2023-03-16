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
});



// EDIT INFOS APPARTEMENT
function editAppart(e) {
    e.preventDefault();
    const values = new FormData(document.getElementById("editAppart"));
    const result = document.getElementById("alert");

    fetch("/app/editAppart", {
        method: "POST",
        url : "/app/editAppart",
        body: values
    }).then((response) => {
        return response.json();
    }).then((data) => {
        if(data.nochange) {
            //
        } else {
            if (data.success === "yes") {
                if ((data.idEtatOut != null || data.idEtatIn != null)) {
                    if (data.idEtatOut != null && data.idEtatIn != null) {
                        result.innerHTML =
                            "<p>Les informations de l'appartement ont été modifier avec succès.</p>" +
                            "<p>Un état des lieux a été créer suite au déménagement du locataire " + data.lastNameOldLoc + " " +
                            data.firstNameOldLoc + " : <a target='_blank' href='/app/appart/etat/editEtat/" + data.idEtatOut + "'>VOIR</a></p>" +

                            "<p>Un bilan des comptes des loyers émis par l'ancien locataire a également été créer : " +
                            "<a target='_blank' href='/app/bilan/" + data.lastNameOldLoc + "'>VOIR</a></p>" +

                            "<p>Un état des lieux a été créer suite à l'emménagement du locataire " + data.lastNameNewLoc + " " +
                            data.firstNameNewLoc + " : <a target='_blank' href='/app/appart/etat/editEtat/" + data.idEtatIn + "'>VOIR</a></p>" +

                            "<p>Un dépôt de garantie a été créer suite à l'emménagement du locataire " + data.lastNameNewLoc + " " +
                            data.firstNameNewLoc + " : <a target='_blank' href='/app/appart/depotGarantie/" + data.idAppart + "'>VOIR</a></p>";
                        result.style.color = "green";
                    } else if (data.idEtatIn == null) {
                        result.innerHTML = "<p>Les informations de l'appartement ont été modifier avec succès.</p>" +
                            "<p>Un état des lieux a été créer suite au déménagement du locataire " + data.lastNameOldLoc + " " +
                            data.firstNameOldLoc + " : <a target='_blank' href='/app/appart/etat/editEtat/" + data.idEtatOut + "'>VOIR</a></p>" +

                            "<p>Un bilan des comptes des loyers émis par l'ancien locataire a également été créer : <a target='_blank' href='/app/bilan/" + data.lastNameOldLoc + "'>VOIR</a></p>";
                        result.style.color = "green";
                    } else {
                        result.innerHTML = "<p>Les informations de l'appartement ont été modifier avec succès.</p>" +
                            "<p>Un état des lieux a été créer suite à l'emménagement du locataire " + data.lastNameNewLoc + " " +
                            data.firstNameNewLoc + " : <a target='_blank' href='/app/appart/etat/editEtat/" + data.idEtatIn + "'>VOIR</a></p>" +

                            "<p>Un dépôt de garantie a été créer suite à l'emménagement du locataire " + data.lastNameNewLoc + " " +
                            data.firstNameNewLoc + " : <a target='_blank' href='/app/appart/depotGarantie/" + data.idAppart + "'>VOIR</a></p>";
                        result.style.color = "green";
                    }
                } else {
                    result.innerText = "Les informations de l'appartement ont été modifier avec succès.";
                    result.style.color = "green";
                    setTimeout(function () {
                        result.innerText = "";
                    }, 5000);
                }
            } else if (data.error === "one") {
                result.innerText = "Opération impossible, le locataire actuel n'est pas en règle sur le paiement de ses loyers.";
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
addEventListenerEditAppart();



// EVENTS
function addEventListenerEditAppart() {
    const form = document.getElementById("editAppart");
    form.addEventListener("submit", editAppart, false);
}