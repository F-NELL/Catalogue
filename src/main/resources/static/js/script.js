// afficher l'image en preview avant validation
function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#imgPreview')
                .attr('src', e.target.result);
            $('#imageUrl')
                .attr('value', input.files[0].name);
            $('#imgPreview').show(); // Afficher l'image une fois qu'elle est chargée
        };

        reader.readAsDataURL(input.files[0]);
    }
}