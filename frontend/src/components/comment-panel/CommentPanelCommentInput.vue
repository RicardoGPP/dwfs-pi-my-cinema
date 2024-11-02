<template>
    <div class="comment-panel-comment-input">
        <Textarea
            v-model="text"
            rows="5"
            cols="30"
            autoResize
        />
        <div>
            <Button
                label="Postar"
                :disabled="!canPost"
                @click="post"
            />
        </div>
    </div>
</template>

<script>
import Textarea from 'primevue/textarea';
import Button from 'primevue/button';

export default {
    name: 'CommentPanelCommentInput',
    components: {
        Textarea,
        Button
    },
    data() {
        return {
            text: ''
        }
    },
    computed: {
        canPost() {
            return this.text !== null
                && this.text !== undefined
                && this.text !== '';
        }
    },
    methods: {
        post() {
            this.$emit('post', this.text);
            this.text = '';
        }
    }
}
</script>

<style lang="scss" scoped>
.comment-panel-comment-input {
    display: flex;
    flex-direction: column;
    gap: 10px;

    & > div {
        display: flex;
        justify-content: flex-end;
    }
}
</style>