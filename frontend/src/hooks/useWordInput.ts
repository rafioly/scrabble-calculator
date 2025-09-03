import { useState, useRef } from 'react';

const TILE_COUNT = 10;

export const useWordInput = () => {
    const [tiles, setTiles] = useState<string[]>(Array(TILE_COUNT).fill(''));
    const inputRefs = useRef<(HTMLInputElement | null)[]>([]);

    const handleTileChange = (e: React.ChangeEvent<HTMLInputElement>, index: number) => {
        const value = e.target.value.toUpperCase().slice(0, 1);
        const newTiles = [...tiles];
        newTiles[index] = value;
        setTiles(newTiles);

        // Move to and select the next input
        if (value && index < TILE_COUNT - 1) {
            inputRefs.current[index + 1]?.select();
        }
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>, index: number) => {
        if (e.key === 'Backspace' && !tiles[index] && index > 0) {
            inputRefs.current[index - 1]?.select();
        } else if (e.key === 'ArrowLeft') {
            e.preventDefault(); // Always prevent default cursor movement
            if (index > 0) {
                inputRefs.current[index - 1]?.select();
            }
        } else if (e.key === 'ArrowRight') {
            e.preventDefault(); // Always prevent default cursor movement
            if (index < TILE_COUNT - 1) {
                inputRefs.current[index + 1]?.select();
            }
        }
    };

    const handleReset = () => {
        setTiles(Array(TILE_COUNT).fill(''));
        inputRefs.current[0]?.focus();
    };

    return { tiles, inputRefs, handleTileChange, handleKeyDown, handleReset };
};
