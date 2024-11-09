<template>
    <div class="movie-dialog">
        <Dialog
            v-if="visible"
            v-model:visible="visible"
            :header="`${action} de filme`"
            :draggable="false"
            modal
            contentStyle="width: 400px; height: 500px">

            <!-- Title -->
            <div class="input">
                <label for="title">
                    *Título:
                </label>
                <InputText
                    id="title"
                    v-model="movie.target.title"
                    placeholder="Digite o título"
                />
            </div>

            <!-- Tagline -->
            <div class="input">
                <label for="tagline">
                    *Tagline:
                </label>
                <InputText
                    id="tagline"
                    v-model="movie.target.tagline"
                    placeholder="Digite a tagline"
                />
            </div>

            <!-- Release date -->
            <div class="input">
                <label for="releaseDate">
                    *Data de lançamento:
                </label>
                <DatePicker
                    id="releaseDate"
                    v-model="releaseDate"
                    dateFormat="dd/mm/yy"
                    showIcon
                    fluid
                    iconDisplay="input"
                    placeholder="Digite a data de lançamento"
                />
            </div>

            <!-- Runtime -->
            <div class="input">
                <label for="runtime">
                    *Duração (min):
                </label>
                <InputNumber
                    inputId="runtime"
                    v-model="movie.target.runtime"
                    fluid
                    showButtons
                />
            </div>

            <!-- Genres -->
            <div class="input">
                <label for="genres">
                    *Gênero(s):
                </label>
                <InputText
                    id="genres"
                    v-model="genres"
                    placeholder="Digite os gêneros"
                />
            </div>

            <!-- Poster path -->
            <div class="input">
                <label for="posterPath">
                    *Caminho do pôster:
                </label>
                <InputText
                    id="posterPath"
                    v-model="movie.target.posterPath"
                    placeholder="Digite o caminho do pôster"
                />
            </div>

            <!-- Backdrop path -->
            <div class="input">
                <label for="backdropPath">
                    *Caminho do plano de fundo:
                </label>
                <InputText
                    id="backdropPath"
                    v-model="movie.target.backdropPath"
                    placeholder="Digite o caminho do plano de fundo"
                />
            </div>

            <!-- Overview -->
            <div class="input">
                <label for="overview">
                    *Sinopse:
                </label>
                <Textarea
                    v-model="movie.target.overview"
                    id="overview"
                    rows="8"
                    placeholder="Digite a sinopse"
                />
            </div>

            <!-- Actions -->
            <template #footer>
                <Button
                    label="Aplicar"
                    :disabled="!canApply"
                    @click="apply"
                />
                <Button
                    label="Cancelar"
                    class="p-button-secondary"
                    @click="close"
                />
            </template>
        </Dialog>
    </div>
</template>

<script>
import InputText from 'primevue/inputtext';
import DatePicker from 'primevue/datepicker';
import InputNumber from 'primevue/inputnumber';
import Textarea from 'primevue/textarea';
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import _ from 'lodash';

export default {
    components: {
        InputText,
        DatePicker,
        InputNumber,
        Textarea,
        Dialog,
        Button
    },
    data() {
        return {
            visible: false,
            callback: null,
            mode: null,
            movie: {
                source: null,
                target: null
            }
        }
    },
    computed: {
        action() {
            if (this.mode === 'create') {
                return 'Criação';
            }
            return 'Edição';
        },
        releaseDate: {
            get() {
                const stringReleaseDate = this.movie.target.releaseDate;

                if (_.isEmpty(stringReleaseDate)) {
                    return null;
                }

                const parts = stringReleaseDate.split('-');

                const year = Number(parts[0]);
                const month = Number(parts[1]);
                const day = Number(parts[2]);

                return new Date(year, month - 1, day);
            },
            set(releaseDate) {
                let stringReleaseDate = null;

                if (releaseDate) {
                    const year = releaseDate.getFullYear();
                    const month = String(releaseDate.getMonth() + 1).padStart(2, '0');
                    const day = String(releaseDate.getDate()).padStart(2, '0');

                    stringReleaseDate = `${year}-${month}-${day}`;
                }

                this.movie.target.releaseDate = stringReleaseDate;
            }
        },
        genres: {
            get() {
                const arrayGenres = this.movie.target.genres;

                if (_.isEmpty(arrayGenres)) {
                    return null;
                }

                return arrayGenres.join(', ');
            },
            set(genres) {
                let arrayGenres = [];

                if (!_.isEmpty(genres)) {
                    arrayGenres = genres.split(',').map((genre) => genre.trim());
                }

                this.movie.target.genres = arrayGenres;
            }
        },
        canApply() {
            const requiredFields = [
                'title',
                'tagline',
                'releaseDate',
                'runtime',
                'genres',
                'posterPath',
                'backdropPath',
                'overview'
            ];

            for (let requiredField of requiredFields) {
                if (this.isEmpty(this.movie.target[requiredField])) {
                    console.log(requiredField, this.movie.target[requiredField]);
                    return false;
                }
            }

            return true;
        }
    },
    methods: {
        open(mode, movie, callback) {
            this.mode = mode;

            if (mode === 'create') {
                this.movie.target = movie;
            } else {
                this.movie.source = movie;
                this.movie.target = _.cloneDeep(movie);
            }

            this.callback = callback;
            this.visible = true;
        },
        close() {
            this.visible = false;
        },
        apply() {
            this.callback(this.movie.target);
            this.close();
        },
        isEmpty(value) {
            if (_.isNumber(value)) {
                return value === null || value === undefined;
            }
            return _.isEmpty(value);
        }
    }
};
</script>

<style lang="scss" scoped>
.input {
    display: flex;
    flex-direction: column;
    gap: 2px;
    margin-bottom: 1rem;

    & > label {
        font-weight: bold;
    }
}
</style>