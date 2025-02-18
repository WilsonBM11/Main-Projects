var num_group = 0;

function max_selection(num_player, phase) {
    switch (num_player) {
        case '5':
            switch (phase) {
                case '1':
                    return 2;
                case '2':
                    return 3;
                case '3':
                    return 2;
                case '4':
                    return 3;
                case '5':
                    return 3;
            }
            break;
        case '6':
            switch (phase) {
                case '1':
                    return 2;
                case '2':
                    return 3;
                case '3':
                    return 4;
                case '4':
                    return 3;
                case '5':
                    return 4;
            }
            break;
        case '7':
            switch (phase) {
                case '1':
                    return 2;
                case '2':
                    return 3;
                case '3':
                    return 3;
                case '4':
                    return 4;
                case '5':
                    return 4;
            }
            break;
        case '8':
            switch (phase) {
                case '1':
                    return 3;
                case '2':
                    return 4;
                case '3':
                    return 4;
                case '4':
                    return 5;
                case '5':
                    return 5;
            }
            break;
        case '9':
            switch (phase) {
                case '1':
                    return 3;
                case '2':
                    return 4;
                case '3':
                    return 4;
                case '4':
                    return 5;
                case '5':
                    return 5;
            }
            break;
        case '10':
            switch (phase) {
                case '1':
                    return 3;
                case '2':
                    return 4;
                case '3':
                    return 4;
                case '4':
                    return 5;
                case '5':
                    return 5;
            }
            break;
    }

}

$("#checklistForm input[type=checkbox]").change(function () {
    const checkboxes_selected = $("#checklistForm input[type=checkbox]:checked");
    if (checkboxes_selected.length > num_group) {
        this.checked = false;
    }
});

function showPopUpPG(num_player, phase) {
    num_group = max_selection(num_player, phase)
    $("#popup_pg_container").modal('show');
}

function showPopUpVG() {
    $("#popup_vote_container").modal('show');
}


//Obtiene los jugadores seleccionados y propone el grupo mediante el controlador
function propose_group(gameId, roundId, password, player, server) {

    const checkboxes = $("input[type=checkbox]:checked");

    //Se asegura de que se seleccionen los jugadores necesarios
    var group;
    if (checkboxes.length === num_group) {
        //Obtiene los jugadores
        group = checkboxes.map(function () {
            return $(this).val();
        }).get();

        //Hace el llamado al controlador y muestra el mensaje de respuesta
        var url = "/ContaminaDOS/ProposeGroup";
        $.ajax({
            url: url,
            type: 'GET',
            data: {
                group_p: JSON.stringify(
                    {
                        group: group
                    }
                ),
                gameId: gameId,
                roundId: roundId,
                password: password,
                player: player,
                server: server
            },
            dataType: 'json',
            success: function (response) {
                alertUser(response);
            },
            error: function (error) {
                console.error("Error:", error);
            }
        });
    } else {
        Swal.fire({
            title: '¡Alerta!',
            text: 'Selecciona Exactamente ' + num_group + ' Elementos',
            icon: 'warning',
            confirmButtonText: '¡Entendido!'
        });
    }
}

//Votar por el grupo propuesto por el lider
function votar(vote_p, gameId, roundId, password, player, server) {

    var url = '/ContaminaDOS/VoteGroup';
    $.ajax({
        url: url,
        type: 'GET',
        data: {
            vote_p: JSON.stringify({ vote: vote_p }),
            gameId: gameId,
            roundId: roundId,
            password: password,
            player: player,
            server: server
        },
        dataType: 'json',
        success: function (response) {
            alertUser(response);
        },
        error: function (error) {
            console.error('Error:', error);
        }
    });
}

function gameStart(gameId, password, player, server) {
    var url = "/ContaminaDOS/GameStart";
    url += "?gameId=" + gameId + "&password=" + password + "&player=" + player + "&server=" + server;

    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        success: function (response) {
            alertUser(response);
        },
        error: function (error) {
            console.error("Error:", error);
        }
    });
}

function submitAction(action_p, gameId, roundId, password, player, server) {

    var url = '/ContaminaDOS/SubmitAction';
    $.ajax({
        url: url,
        type: 'GET',
        data: {
            action_p: JSON.stringify(
                {
                    action: action_p
                }
            ),
            gameId: gameId,
            roundId: roundId,
            password: password,
            player: player,
            server: server
        },
        dataType: 'json',
        success: function (response) {
            alertUser(response);
        },
        error: function (error) {
            console.error('Error:', error);
        }
    });
}

function alertUser(response) {
    if (response.status < 400) {
        Swal.fire({
            title: '¡Alert!',
            text: response.msg,
            icon: 'success',
            confirmButtonText: '¡Understood!'
        });
        setTimeout(function () {
            $('#update_view').click();
        }, 5000);
    } else {
        Swal.fire({
            title: '¡Alert!',
            text: response.msg,
            icon: 'error',
            confirmButtonText: '¡Understood!'
        });
    }
}

