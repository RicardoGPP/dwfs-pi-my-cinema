<template>
    <Dialog
        class="movie-dialog"
        header="Detalhes do Filme"
        v-model:visible="dialogVisible"
        modal
        :closable="false">
        <div class="form-group">
            <label for="title">
                Título:
            </label>
            <InputText
                v-model="movie.title"
                id="title"
                placeholder="Digite o título do filme"
            />
        </div>
        <div class="form-group">
            <label for="overview">
                Sinopse:
            </label>
            <Textarea
                v-model="movie.overview"
                id="overview"
                rows="3"
                placeholder="Digite a sinopse do filme"
            />
        </div>
        <div class="form-group">
            <label for="tagline">
                Tagline:
            </label>
            <InputText
                v-model="movie.tagline"
                id="tagline"
                placeholder="Digite a tagline do filme"
            />
        </div>
        <div class="form-group">
            <label for="language">
                Idioma:
            </label>
            <InputText
                v-model="movie.language"
                id="language"
                placeholder="Digite o idioma do filme"
            />
        </div>
        <div class="form-group">
            <label for="genres">
                Gêneros:
            </label>
            <InputText
                v-model="genres"
                id="genres"
                placeholder="Digite os gêneros separados por vírgula"
            />
        </div>
        <div class="form-group">
            <label for="releaseDate">
                Data de Lançamento:
            </label>
            <InputText
                v-model="movie.releaseDate"
                id="releaseDate"
                placeholder="Digite a data de lançamento (YYYY-MM-DD)"
            />
        </div>
        <div class="form-group">
            <label for="runtime">
                Duração (min):
            </label>
            <InputText
                v-model="movie.runtime"
                id="runtime"
                placeholder="Digite a duração do filme"
            />
        </div>
        <div class="form-group">
            <label for="posterPath">
                Caminho do Poster:
            </label>
            <InputText
                v-model="movie.posterPath"
                id="posterPath"
                placeholder="Digite o caminho da imagem do poster"
            />
        </div>
        <div class="form-group">
            <label for="trailerUrl">
                URL do Trailer:
            </label>
            <InputText
                v-model="movie.trailerUrl"
                id="trailerUrl"
                placeholder="Digite a URL do trailer"
            />
        </div>
        <div class="form-group">
            <label for="cast">
                Elenco:
            </label>
            <InputText
                v-model="cast"
                id="cast"
                placeholder="Digite o elenco separado por vírgula"
            />
        </div>
        <template #footer>
            <Button
                label="Salvar"
                icon="pi pi-check"
                @click="saveMovie"
            />
            <Button
                label="Cancelar"
                icon="pi pi-times"
                class="p-button-secondary"
                @click="closeDialog"
            />
        </template>
    </Dialog>
</template>

<script>
import InputText from 'primevue/inputtext';
import Textarea from 'primevue/textarea';
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';

export default {
    components: {
        InputText,
        Textarea,
        Dialog,
        Button
    },
    data() {
        return {
            dialogVisible: false,
            movie: {
                title: '',
                overview: '',
                tagline: '',
                language: '',
                releaseDate: '',
                runtime: '',
                posterPath: '',
                trailerUrl: ''
            },
            genres: '',
            cast: ''
        };
    },
    methods: {
        openDialog(movie = null) {
            if (movie) {
                this.movie = { ...movie };
                this.genres = movie.genres ? movie.genres.join(', ') : '';
                this.cast = movie.cast ? movie.cast.join(', ') : '';
            } else {
                this.movie = {
                    title: '',
                    overview: '',
                    tagline: '',
                    language: '',
                    releaseDate: '',
                    runtime: '',
                    posterPath: '',
                    trailerUrl: ''
                };
                this.genres = '';
                this.cast = '';
            }
            this.dialogVisible = true;
        },
        saveMovie() {
            this.movie.genres = this.genres.split(',').map(genre => genre.trim());
            this.movie.cast = this.cast.split(',').map(member => member.trim());
            this.$emit('save', { ...this.movie });
            this.dialogVisible = false;
        },
        closeDialog() {
            this.dialogVisible = false;
        }
    }
};
</script>

<style scoped>
.form-group {
    display: flex;
    flex-direction: column;
    gap: 2px;
    margin-bottom: 1rem;
}
</style>