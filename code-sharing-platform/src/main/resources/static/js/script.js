function send() {
    let object = {
        "code": document.getElementById("code_snippet").value,
        "views": document.getElementById("views_restriction").value,
        "time": document.getElementById("time_restriction").value
    };

    let json = JSON.stringify(object);

    let xhr = new XMLHttpRequest();
    xhr.open("POST", '/api/code/new', false)
    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    xhr.send(json);
    
    if (xhr.status == 200) {
        let response = JSON.parse(xhr.response);
        let uuid = response["id"];
        alert(`Success! UUID: "${uuid}"`);
    }
}