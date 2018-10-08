document.getElementById('fileUpload').addEventListener('change', function() {
    document.getElementById('file-input-name').value = this.value;
});

function redirect_home() {
    setTimeout(function () {
        document.location.pathname = "/";
    }, 1000);
};