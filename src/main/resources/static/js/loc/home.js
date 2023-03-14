// INIT
window.addEventListener('load', function() {
    // HEADER
    async function loadHeader() {
        await fetch("/app/header").then(res => res.text()).then(data => {
            document.getElementById("header").innerHTML = data;
            document.getElementById("head-loc").setAttribute("class", "active");
        }).then(() => {
        })}

    // DATE ET HEURE
    function date_heure(id) {
        date = new Date;
        annee = date.getFullYear();
        moi = date.getMonth();
        mois = ['janvier', 'f&eacute;vrier', 'mars', 'avril', 'mai', 'juin', 'juillet', 'ao&ucirc;t', 'septembre', 'octobre', 'novembre', 'd&eacute;cembre'];
        j = date.getDate();
        jour = date.getDay();
        jours = ['dimanche', 'lundi', 'mardi', 'mercredi', 'jeudi', 'vendredi', 'samedi'];
        h = date.getHours();
        if(h<10)
        {
            h = "0"+h;
        }
        m = date.getMinutes();
        if(m<10)
        {
            m = "0"+m;
        }

        document.getElementById(id).innerHTML = 'Nous sommes le ' + jours[jour] + ' ' + j + ' ' + mois[moi] + ' ' + annee + ' et il est ' + h + ':' + m;
        return true;
    }

    loadHeader();
    reload();
    date_heure("date_heure");
});



function reload() {
    // LOCATAIRES
    async function loadLocs() {
        await fetch("/app/data/listOfLocs").then(res => res.text()).then(data => {
            document.getElementById("data").innerHTML = data;
        }).then(() => {
        })}

    loadLocs();
}



// RESET MODULE { RECHERCHE LOCATAIRE }
document.getElementById("rchLoc").addEventListener("click", function() {
    document.getElementById("about").style.display = "none";
    document.querySelector("input[name='name']").value = "";
}, false);



// RESET MODULE { CREER LOCATAIRE }
document.getElementById("close").addEventListener("click", function() {
    document.getElementById("error").innerText = "";
    document.querySelector("input[name='lastName']").value = "";
    document.querySelector("input[name='firstName']").value = "";
    document.querySelector("input[name='email']").value = "";
    document.querySelector("input[name='tel']").value = "";
}, false);



// CREATE PROFIL LOCATAIRE
function createLoc(e) {
    e.preventDefault();
    const values = new FormData(document.getElementById("createLoc"));
    const result = document.getElementById("error");

    fetch("/app/createLoc", {
        method: "POST",
        url : "/app/createLoc",
        body: values
    }).then((response) => {
        return response.json();
    }).then((data) => {
        if (data.success == "yes") {
            result.innerText = "Le profil du locataire a été créer avec succès.";
            result.style.color = "green";
            setTimeout(function() { result.innerText = ""; }, 5000);
            document.querySelector("input[name='lastName']").value = "";
            document.querySelector("input[name='firstName']").value = "";
            document.querySelector("input[name='email']").value = "";
            document.querySelector("input[name='tel']").value = "";
            reload();
        } else if (data.error == "one") {
            result.innerText = "Un locataire possède déjà cette adresse email.";
            result.style.color = "red";
        } else if (data.error == "two") {
            result.innerText = "Ce profil locataire existe déjà.";
            result.style.color = "red";
        } else {
            result.innerText = data.msgError;
            result.style.color = "red";
        }
    }).catch(error => {
        console.log('Erreur : ' + error.message);
    })
}
addEventListenerCreateLoc();



// RESEARCH LOCATAIRE
function researchLoc(e) {
    e.preventDefault();
    const values = new FormData(document.getElementById("researchLoc"));

    fetch("/app/researchLoc", {
        method: "POST",
        url : "/app/researchLoc",
        body: values
    }).then((response) => {
        return response.json();
    }).then((data) => {
        if(data.success == "yes") {
            document.getElementById("about").style.display = "block";
            document.getElementById("id").innerText = data.id;
            document.getElementById("nom").innerText = data.nom;
            document.getElementById("prenom").innerText = data.prenom;
            document.getElementById("email").innerText = data.email;
            document.querySelector("input[name='name']").value = "";
            document.getElementById("urlEdit").href = data.url;
            document.getElementById("urlBilan").href = data.urlBilan;
        } else {
            document.getElementById("about").style.display = "none";
        }
    }).catch(error => {
        console.log('Erreur : ' + error.message);
    })
}
addEventListenerResearchLoc();



// EVENTS
function addEventListenerCreateLoc() {
    const form = document.getElementById("createLoc");
    form.addEventListener("submit", createLoc, false);
}
function addEventListenerResearchLoc() {
    const form = document.getElementById("researchLoc");
    form.addEventListener("submit", researchLoc, false);
}