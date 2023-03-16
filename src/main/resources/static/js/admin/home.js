// INIT
window.addEventListener('load', function() {
    // HEADER
    async function loadHeader() {
        await fetch("/app/header").then(res => res.text()).then(data => {
            document.getElementById("header").innerHTML = data;
            document.getElementById("head-admin").setAttribute("class", "active");
        }).then(() => {
        })}

    loadHeader().then(r => r);
    reload();
});



function reload() {
    // USERS
    async function loadUsers() {
        await fetch("/app/admin/data/listOfUsers").then(res => res.text()).then(data => {
            document.getElementById("data").innerHTML = data;
        }).then(() => {
    })}
    loadUsers().then(r => r);
}



// RESET MODULE { RECHERCHE USER }
document.getElementById("rchUser").addEventListener("click", function() {
    document.getElementById("about").style.display = "none";
    document.querySelector("input[name='name']").value = "";
}, false);



// RESET MODULE { CREATE USER }
document.getElementById("close").addEventListener("click", function() {
    document.getElementById("error").innerText = "";
    document.querySelector("input[name='lastName']").value = "";
    document.querySelector("input[name='firstName']").value = "";
}, false);



// CREATE USER
function createUser(e) {
    e.preventDefault();
    const values = new FormData(document.getElementById("createUser"));
    const result = document.getElementById("error");

    fetch("/app/admin/createUser", {
        method: "POST",
        url : "/app/admin/createUser",
        body: values
    }).then((response) => {
        return response.json();
    }).then((data) => {
        if(data.success === "yes") {
            result.innerHTML = "<p>Le compte utilisateur a été créer avec succès.</p>" +
                "<p>Voici la clé d'enregistrement à transmettre à l'utilisateur concerné" +
                "<input type='text' style='width: 100%;' value='"+data.key+"' readonly /></p>";
            result.style.color = "green";
            document.querySelector("input[name='lastName']").value = "";
            document.querySelector("input[name='firstName']").value = "";
            reload();
        } else if(data.error === "one") {
            result.innerText = "Une session employé est déjà enregistré sous ce nom.";
            result.style.color = "red";
        } else {
            result.innerText = data.msgError;
            result.style.color = "red";
        }
    }).catch(error => {
        console.log('Erreur : ' + error.message);
    })
}
addEventListenerCreateUser();



// RECHERCHE USER
function researchUser(e) {
    e.preventDefault();
    const values = new FormData(document.getElementById("researchUser"));

    fetch("/app/admin/researchUser", {
        method: "POST",
        url : "/app/admin/researchUser",
        body: values
    }).then((response) => {
        return response.json();
    }).then((data) => {
        if(data.success === "yes") {
            document.getElementById("about").style.display = "block";
            document.getElementById("id").innerText = data.id;
            const nomM = data.nom.toString().toUpperCase();
            document.getElementById("nom").innerText = nomM;
            document.getElementById("prenom").innerText = data.prenom;
            document.getElementById("pseudo").innerText = data.pseudo;
            if(data.role === "ADMIN") {
                document.getElementById("role").innerText = "Gérant";
            } else {
                document.getElementById("role").innerText = "Employé";
                $("#role").text("Employé");
            }
            document.querySelector("input[name='name']").value = "";
            document.getElementById("urlEdit").href = data.urlEdit;
            if (data.itsMe === "yes") {
                document.getElementById("btnDelete").setAttribute("disabled", "true");
            } else {
                document.getElementById("btnDelete").removeAttribute("disabled");
                document.getElementById("formDelete").setAction("./deleteUser");
                document.getElementById("inputDelete").value = data.id;
            }
        }
    }).catch(error => {
        console.log('Erreur : ' + error.message);
    })
}
addEventListenerResearchUser();



// EVENTS
function addEventListenerCreateUser() {
    const form = document.getElementById("createUser");
    form.addEventListener("submit", createUser, false);
}
function addEventListenerResearchUser() {
    const form = document.getElementById("researchUser");
    form.addEventListener("submit", researchUser, false);
}