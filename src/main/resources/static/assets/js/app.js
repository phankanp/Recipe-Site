$('#add-ingredient').click(function () {
    let ingredientCount = $('.ingredient-row').length;
    let ingredientRow = '<div class="ingredient-row">' +
        '<div >' +
        '<p> <input class="form-control add" placeholder="Ingredient Name" type="text" id="ingredients' + ingredientCount + '.item" name="ingredients[' + ingredientCount + '].name" />' +
        '</p> </div>' +
        '<div>' +
        '<p> <input class="form-control add" placeholder="Ingredient Condition" type="text" id="ingredients' + ingredientCount + '.ingredientCondition" name="ingredients[' + ingredientCount + '].ingredientCondition" />' +
        '</p> </div>' +
        '<div">' +
        '<p> <input class="form-control add" placeholder="Ingredient Quantity" type="text" id="ingredients' + ingredientCount + '.quantity" name="ingredients[' + ingredientCount + '].quantity" />' +
        '</p>' +
        '<hr class="addRows">' +
        '</div>' +
        '</div>';
    $("#add-ingredient-row").before(ingredientRow);
});

$('#add-step').click(function () {
    let stepCount = $('.step-row').length;
    let stepRow = '<div class="step-row">' +
        '<div>' +
        '<hr class="addRows">' +
        '<p>' +
        '<input class="form-control add" placeholder="Step Description" type="text" id="steps' + stepCount + '.stepName" name="steps[' + stepCount + '].name" />' +
        '</p>' +
        '</div>' +
        '</div>'
    $("#add-step-row").before(stepRow);
});

$(document).on("change", "#select-category", function () {
    let category = $('#select-category').val();
    window.location.href = '/recipes/category/' + category;
});

$('#search').on('keydown', function (e) {
    if (e.which === 13 && this.value.length < 13) {
        $(this).prop("disabled", true);
        let search = $('#search').val();
        window.location.href = '/recipes/search?' + 'searchq' + '=' + search;
    }
});

$(document).ready(function () {
    let recipeId = "";

    $('.favorite-button-index').each(function () {
        $(this).click(function () {
            recipeId = $(this).attr('id');
        });
    });

    $(".favorite").on("submit", function (e) {
        e.preventDefault();

        recipeUrlId = recipeId.substring(21)

        $.post(`/recipes/${recipeUrlId}/favorite`, function (data) {
            let imageUrl = (data.favorited) ? '/assets/images/favorited.svg' : '/assets/images/favorite.svg'
            $('#' + recipeId + '>img').attr('src', imageUrl);
        })
            .fail(function (request) {
                window.location.href = '/login';
            })
    })
})

$(".favorite-detail").on("submit", function (e) {

    e.preventDefault();

    $.post(this.action, function (data) {
        let imageUrl = (data.favorited) ? '/assets/images/favorited.svg' : '/assets/images/favorite.svg'
        $('#favorite-button-detail>img').attr('src', imageUrl);
    })

})


$('.upvote').click(function (e) {
    const direction = 1;
    const recipeId = $(this).attr("data-id")
    const voteSum = $(".votecount-" + recipeId).html()

    $.get(`http://localhost:8080/vote/recipe/${recipeId}/direction/${direction}/votecount/${voteSum}`, function (data) {
        $(".votecount-" + recipeId).html(data)
    })

    $('.upvote-' + recipeId).attr("disabled", true);
    $('.downvote-' + recipeId).attr("disabled", false);

    return false
})

$('.downvote').click(function (e) {
    const direction = -1;
    const recipeId = $(this).attr("data-id")
    const voteSum = $(".votecount-" + recipeId).html()

    $.get(`http://localhost:8080/vote/recipe/${recipeId}/direction/${direction}/votecount/${voteSum}`, function (data) {
        $(".votecount-" + recipeId).html(data)
    })

    $('.upvote-' + recipeId).attr("disabled", false);
    $('.downvote-' + recipeId).attr("disabled", true);


    return false
})

let max = 50;
let tot, str;
$('.test').each(function () {
    str = String($(this).html());
    tot = str.length;
    str = (tot <= max)
        ? str
        : str.substring(0, (max + 1)) + "...";
    $(this).html(str);
});


$(document).ready(function () {
    $('#addComment').click(function (e) {
        e.preventDefault()
        ajaxComment()
    })

    function ajaxComment() {
        let formData = {
            body: $('#comment').val(),
            recipe_id: $("#modelAttr").val()
        }
        let id = $("#modelAttr").val()
        console.log(formData)
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: `http://localhost:8080/recipe/comments` + '?' + 'recipe_id=' + id,
            data: JSON.stringify(formData),
            dataType: 'json',
            success: function (data) {
                let commentadd = `<div class="row comment"><div class="col-12"><span class="comment-user">${data.createdBy} says:</span><p class="body">${data.commentBody}</p><hr></div></div>`;

                $(".comments").append(commentadd)
            },
            error: (function (request) {
                window.location.href = '/login';
            })
        })
        resetData()
    }

    function resetData() {
        $('#comment').val("")
    }
})


$(document).ready(function () {
    $('.delete-button').click(function (e) {
        let recipeId = $(this).attr("data-id")
        let button = $(this)

        e.preventDefault();
        ajaxdelete(recipeId, button)
    })

    function ajaxdelete(recipeId, button) {
        $.ajax({
            type: "DELETE",
            url: `http://localhost:8080/recipes/${recipeId}`,
            success: function (data) {
                $("." + recipeId).closest(".col-md-6").remove();
            },
            error: (function (request) {
                console.log(request.getResponseHeader("error"))

                if (request.getResponseHeader("error") === null) {
                    window.location.href = '/login';
                } else {
                    window.location.href = '/403';
                }
            })
        })
    }
})

function categoryPageHideSection() {
    if ($('.all').hasClass('active')) {
        $('#all').fadeOut();
    } else if ($('.breakfast').hasClass('active')) {
        $('#breakfast').fadeOut();
    } else if ($('.lunch').hasClass('active')) {
        $('#lunch').fadeOut();
    } else if ($('.dinner').hasClass('active')) {
        $('#dinner').fadeOut();
    } else if ($('.dessert').hasClass('active')) {
        $('#dessert').fadeOut();
    }
}

$(".all").click(function (e) {
    categoryPageHideSection()

    $('.sort').fadeOut();

    $('.nav li a.active').removeClass('active');
    $(this).addClass('active');

    $('#all').fadeIn('slow');
    return false;
})

$(".breakfast").click(function (e) {
    categoryPageHideSection()

    $('.sort').fadeOut();

    $('.nav li a.active').removeClass('active');
    $(this).addClass('active');

    $('#breakfast').fadeIn('slow');
    return false;
})

$(".lunch").click(function (e) {
    categoryPageHideSection()

    $('.sort').fadeOut();

    $('.nav li a.active').removeClass('active');
    $(this).addClass('active');

    $('#lunch').fadeIn('slow');
    return false;
})

$(".dinner").click(function (e) {
    categoryPageHideSection()

    $('.sort').fadeOut();

    $('.nav li a.active').removeClass('active');
    $(this).addClass('active');

    $('#dinner').fadeIn('slow');
    return false;
})

$(".dessert").click(function (e) {
    categoryPageHideSection()

    $('.sort').fadeOut();

    $('.nav li a.active').removeClass('active');
    $(this).addClass('active');

    $('#dessert').fadeIn('slow');
})

$(".search").click(function (e) {

    $('.sort').fadeIn('slow');
    return false;
})







